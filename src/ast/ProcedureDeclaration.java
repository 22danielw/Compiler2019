package ast;

import emitter.Emitter;

import java.util.List;

/**
 * A class that represents a procedure declaration. Stores
 * a name, statement, and a list of Strings (parameters).
 *
 * @author Daniel Wu
 * @version 10/10/19
 */
public class ProcedureDeclaration extends Statement
{
    private String name;
    private Statement declaration;
    private List<String> params;
    private List<String> localVars;

    /**
     * Constructs a ProcedureDeclaration class by initializing the instance
     * variables containing the name, Statement, and List of formal params.
     *
     * @param n the name of the ProcedureDeclaration
     * @param d the Statement stored by the ProcedureDeclaration
     * @param p the List of parameters stored by the ProcedureDeclaration
     * @param l the List of local variables stored by the Procedure Declaration
     */
    public ProcedureDeclaration(String n, Statement d, List<String> p, List<String> l)
    {
        name = n;
        declaration = d;
        params = p;
        localVars = l;
    }

    /**
     * A getter that returns the name of the ProcedureCall.
     *
     * @return the instance variable name
     */
    public String getName()
    {
        return name;
    }

    /**
     * A getter that returns the Statement storedy by the ProcedureCall.
     *
     * @return the instance variable declaration
     */
    public Statement getDeclaration()
    {
        return declaration;
    }

    /**
     * A getter that returns the parameters stored in the ProcedureCall.
     *
     * @return the instance variable params
     */
    public List<String> getParams()
    {
        return params;
    }

    /**
     * A getter that returns the local variables stored in the ProcedureCall.
     *
     * @return the instance variable params
     */
    public List<String> getLocalVars()
    {
        return localVars;
    }

    /**
     * Compiles a ProcedureDeclaration into MIPS code. Sets
     * the procedure context for emitter, then pushes an empty return
     * value and the return address onto the stack. Then, pushes all the
     * local variables onto the stack, compiles the statement, then
     * pops everything off the stack and jumps return. Then, the
     * procedure context is cleared.
     *
     * @param e the Emitter emitting the code to be compiled
     */
    public void compile(Emitter e)
    {
        e.setProcedureContext(this);
        e.emit("proc" + name + ":");
        e.emit("li $v0 0");
        e.emitPush("$v0");

        e.emitPush("$ra");

        for (String s : localVars)
        {
            e.emit("li $v0 0");
            e.emitPush("$v0");
        }

        declaration.compile(e);

        for (String s : localVars)
        {
            e.emitPop("$t0");
        }

        e.emitPop("$ra");
        e.emitPop("$v0");
        e.emit("jr $ra");
        e.clearProcedureContext();
    }

}
