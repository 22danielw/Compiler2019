package emitter;

import ast.ProcedureDeclaration;

import java.io.*;

/**
 * A class that emits text/MIPS code to a file of a given name.
 *
 * @author Ms. Datar
 * @author Daniel Wu
 * @version 11/29/2019
 * @version 12/12/2019
 */
public class Emitter
{
    private PrintWriter out;
    private int nextid = 0;
    private ProcedureDeclaration currentProc;
    private int excessStackHeight = 0;

    /**
     * Creates an emitter for writing to a new file with given name.
     *
     * @param outputFileName the name of the file the emitter outputs to
     */
    public Emitter(String outputFileName)
    {
        try
        {
            out = new PrintWriter(new FileWriter(outputFileName), true);
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    /**
     * Prints one line of code to file (with non-labels indented).
     *
     * @param code the code being emitted to the file
     */
    public void emit(String code)
    {
        if (!code.endsWith(":"))
            code = "\t" + code;
        out.println(code);
    }

    /**
     * Closes the file. Should be called after all calls to emit.
     */
    public void close()
    {
        out.close();
    }

    /**
     * Emits code that pushes the value in a given register
     * onto the stack. Increments the excess stack height.
     *
     * @param reg the register whose value is being stored
     */
    public void emitPush(String reg)
    {
        emit("subu $sp $sp 4");
        emit("sw " + reg + " ($sp)");
        excessStackHeight++;
    }

    /**
     * Emits code that pops the top value of a stack and store
     * it into a given register. Also subtracts one from
     * the excess stack height.
     *
     * @param reg the register to where the value at the top of the
     *            stack will be stored.
     */
    public void emitPop(String reg)
    {
        emit("lw " + reg + " ($sp)");
        emit("addu $sp $sp 4");
        excessStackHeight--;
    }

    /**
     * Returns the ID of the next label to be attached to a label
     * for if/while statements.
     *
     * @return an integer representing the next label ID
     */
    public int nextLabelID()
    {
        nextid++;
        return nextid;
    }

    /**
     * Sets the current procedure context to proc. Procedure context
     * represents the current procedure being compiled.
     *
     * @param proc the procedure currently being compiled
     */
    public void setProcedureContext(ProcedureDeclaration proc)
    {
        currentProc = proc;
        excessStackHeight = 0;
    }

    /**
     * Clears the procedure context by setting the current procedure to null.
     * Called whenever the current procedure has finished compiling.
     */
    public void clearProcedureContext()
    {
        currentProc = null;
    }

    /**
     * Returns whether or not the variable name given is a local variable.
     * A variable is considered a local variable if it is the name of
     * procedure, declared in the procedure declaration, or a parameter.
     *
     * @param varName the variable name being searched for
     * @return true if the variable is a local variable; false otherwise
     */
    public boolean isLocalVariable(String varName)
    {
        if (currentProc == null)
            return false;
        else if (varName.equals(currentProc.getName()))
            return true;
        else if (currentProc.getLocalVars().contains(varName))
            return true;
        return currentProc.getParams().contains(varName);
    }

    /**
     * Returns the value of the stack offset that the variable
     * being searched for depending on whether it is a local
     * variable, parameter, or a return value.
     *
     *
     * @precondition localVarName is the name of a local
     *               variable for the procedure currently
     *               being compiled
     *
     * @param localVarName the name of the local variable for
     *                     being accessed in the current Procedure
     * @return the value of the offset to access the local variable/param
     *         identified by localVarName
     */
    public int getOffSet(String localVarName)
    {
        if (currentProc != null && currentProc.getName().equals(localVarName))
        {
            int offSet = 4 * (currentProc.getLocalVars().size() + 1);
            return offSet;
        }
        else if (currentProc.getLocalVars().contains(localVarName))
        {
            int index = currentProc.getLocalVars().indexOf(localVarName);
            System.out.println(4 * (excessStackHeight - index - 3));
            return 4 * (excessStackHeight - index - 3);
        }
        else
        {
            int size = currentProc.getParams().size();
            int index = currentProc.getParams().indexOf(localVarName);
            return 4 * (size + excessStackHeight - index - 1);
        }
    }
}