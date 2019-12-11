package ast;

import emitter.Emitter;

/**
 * A class that represents an assignment statement for
 * the AST. It stores a variable and the associated expression.
 * Extends the abstract Statement class.
 *
 * @author Daniel Wu
 * @version 10/19/2019
 */
public class Assignment extends Statement
{
    private String var;
    private Expression exp;

    /**
     * Constructs an Assignment object with the given parameters. Stores
     * the instance variables representing the name and value of the
     * variable
     *
     * @param v the name of the variable
     * @param e the expression represented by the variable
     */
    public Assignment(String v, Expression e)
    {
        var = v;
        exp = e;
    }

    /**
     * A getter method for the name of the variable
     * @return the instance field var
     */
    public String getVariable()
    {
        return var;
    }

    /**
     * A getter method for expression stored by the assignment
     * @return the instance field exp
     */
    public Expression getExpression()
    {
        return exp;
    }

    /**
     * Translates an assignment statement into assembly code.
     * Writes the value of the expression to a label in memory.
     *
     * @param e the Emitter for the compile method.
     */
    public void compile(Emitter e)
    {
        if (e.isLocalVariable(var))
        {
            exp.compile(e);

                int offSet = e.getOffSet(var);
                e.emit("sw $v0 " + offSet + "($sp)");
        }
        else
        {
            exp.compile(e);
            e.emit("sw $v0 var" + var);
        }
    }
}
