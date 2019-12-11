package ast;
import emitter.Emitter;
import java.util.List;

/**
 * A class that represents a Program containing a List
 * of ProcedureDeclarations and a single statement (typically
 * a BEGIN END block).
 *
 * @author Daniel Wu
 * @version 10/10/19
 */
public class Program
{
    List<String> globalVars;
    List<ProcedureDeclaration> decs;
    Statement statement;

    /**
     * The constructor that initializes the list of ProcedureDeclarations
     * and the Statement.
     *
     * @param g the global list of variables stored in the Program.
     * @param d list of ProcedureDeclarations for the Program
     * @param s the Statement contained by the Program
     */
    public Program(List<String> g, List<ProcedureDeclaration> d, Statement s)
    {
        globalVars = g;
        decs = d;
        statement = s;
    }

    /**
     * Returns the List globalVars containing the declared global variables in
     * the program.
     *
     * @return the instance variable globalVars
     */
    public List<String> getGlobalVars()
    {
        return globalVars;
    }

    /**
     * Getter for the list of ProcedureDeclarations stored by
     * the Program.
     *
     * @return the instance variable decs
     */
    public List<ProcedureDeclaration> getProcedures()
    {
        return decs;
    }

    /**
     * Adds a ProcedureDeclaration to the List stored by
     * the Program.
     *
     * @param dec the Declaration to be added
     */
    public void addProcedure(ProcedureDeclaration dec)
    {
        decs.add(dec);
    }

    /**
     * Getter for the Statement stored by the Program.
     *
     * @return the instance variable statement
     */
    public Statement getStatement()
    {
        return statement;
    }

    /**
     * Converts a program into MIPS assembly code.
     * Initializes .data and .globl main, variables,
     * and normally terminates the program.
     *
     * @param fileName the name of the file that the
     *                 compile method writes to
     */
    public void compile(String fileName)
    {
        Emitter e = new Emitter(fileName);

        e.emit(".data");
        e.emit("newLine: .asciiz \"\\n\"");
        for (String v: globalVars)
            e.emit("var" + v + ": .word 0");

        e.emit(".text");
        e.emit(".globl main");
        e.emit("main:");

        statement.compile(e);

        e.emit("li $v0 10");
        e.emit("syscall");

        for (ProcedureDeclaration dec: decs)
        {
            dec.compile(e);
        }

        e.close();
    }

}
