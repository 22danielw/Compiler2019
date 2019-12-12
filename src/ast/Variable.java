package ast;

import emitter.Emitter;

/**
 * A class that represents a Variable and
 * stores its name.
 *
 * @author Daniel Wu
 * @version 10/19/2019
 */
public class Variable extends Expression
{
    private String name;

    /**
     * Constructs a Variable object with a given name.
     *
     * @param n the name of the Variable object
     */
    public Variable(String n)
    {
        name = n;
    }

    /**
     * A getter for the name stored by the Variable
     *
     * @return the instance field name
     */
    public String getName()
    {
        return name;
    }

    /**
     * Compiles a Variable object in MIPS code. Takes
     * the variable name, assuming that it has already
     * been declared, and sets the value in it into the
     * $v0 register.
     *
     * @param e the Emitter that emits MIPS assembly code
     */
    public void compile(Emitter e)
    {
        if (e.isLocalVariable(name))
        {
            int offSet = e.getOffSet(name);
            e.emit("lw $v0 " + offSet + "($sp)");
        }
        else
        {
            e.emit("la $t0 var" + name);
            e.emit("lw $v0 ($t0)");
        }
    }
}
