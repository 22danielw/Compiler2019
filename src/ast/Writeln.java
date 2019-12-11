package ast;

import emitter.Emitter;

/**
 * A class that represents a Writeln() statement that
 * stores an expression.
 * Extends the abstract Statement class
 *
 * @author Daniel Wu
 * @version 10/19/2019
 */
public class Writeln extends Statement
{
    private Expression exp;

    /**
     * A constructor for the Writeln class that takes and
     * stores an Expression
     *
     * @param e the Expression stored by the Writeln class
     */
    public Writeln(Expression e)
    {
        exp = e;
    }

    /**
     * A getter for the Expression stored by the Writeln
     * object.
     *
     * @return the instance field exp
     */
    public Expression getExpression()
    {
        return exp;
    }

    /**
     * Compiles a print line statement into MIPS assembly
     * code. Prints the String in $v0 by moving it
     * to $a0, printing the String, then printing a newLine
     * character.
     *
     * @param e the Emitter emitting the code to be compiled
     */
    public void compile(Emitter e)
    {
        //compiles the code in the expression
        exp.compile(e);


        e.emit("move $a0 $v0");
        e.emit("li $v0 1");
        e.emit("syscall");

        //enters a new line
        e.emit("la $a0 newLine");
        e.emit("li $v0 4");
        e.emit("syscall");
    }
}
