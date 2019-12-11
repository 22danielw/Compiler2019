package ast;

import emitter.Emitter;
import environment.Environment;

import java.util.List;

/**
 * A class that represents a ProcedureCall. Contains a name
 * and a list of arguments.
 *
 * @author Daniel Wu
 * @version 10/10/19
 */
public class ProcedureCall extends Expression
{
    private String name;
    private List<Expression> args;

    /**
     * Constructs a ProcedureCall class with a name and List of arguments.
     *
     * @param n the name of the ProcedureCall
     * @param a the list of arguments as Expressions
     */
    public ProcedureCall(String n, List<Expression> a)
    {
        name = n;
        args = a;
    }

    /**
     * A getter for the ProcedureCall class that returns the name of
     * the ProcedureCall.
     *
     * @return the instance variable name
     */
    public String getName()
    {
        return name;
    }

    /**
     * A getter for the ProcedureCall class that returns the args of
     * the ProcedureCall.
     *
     * @return the instance variable args
     */
    public List<Expression> getArgs()
    {
        return args;
    }


    /**
     * Compiles a procedure Call into MIPS assembly code.
     * Pushes all the parameters onto the stack, jumps and links
     * to the ProcedureDeclaration label, then pops and
     * discards the parameters.
     *
     * @param e the Emitter that emits MIPS assembly code
     */
    public void compile(Emitter e)
    {
        for (Expression exp: args)
        {
            exp.compile(e);
            e.emitPush("$v0");
        }

        e.emit("jal proc" + name);

        for (int i = 0; i < args.size(); i++)
        {
            e.emitPop("$t0");
        }
    }
}
