package ast;

import emitter.Emitter;

/**
 * A class that represents a binary operation between two expressions.
 * Extends the abstract Expression class.
 *
 * @author Daniel Wu
 * @version 10/19/2019
 */
public class BinOp extends Expression
{
    private String op;
    private Expression exp1;
    private Expression exp2;

    /**
     * A constructor for a BinOp object. Assigns an operator and two
     * expressions as instance variables.
     * @param operator the operator for the binary operation
     * @param e1 the first expression in the operation
     * @param e2 the second expression in the operation
     */
    public BinOp(String operator, Expression e1, Expression e2)
    {
        op = operator;
        exp1 = e1;
        exp2 = e2;
    }

    /**
     * A getter that returns the operator for the BinOp class
     * as a String.
     *
     * @return the instance field op
     */
    public String getOperator()
    {
        return op;
    }

    /**
     * A getter that returns the first expression
     * stored by the BinOp object
     *
     * @return the instance field exp1
     */
    public Expression getExpression1()
    {
        return exp1;
    }

    /**
     * A getter that returns the second expression
     * stored by the BinOp object
     *
     * @return the instance field exp2
     */
    public Expression getExpression2()
    {
        return exp2;
    }


    /**
     * Converts a BinOp expression to MIPS assembly code.
     * Stores the final value in the $v0 register.
     *
     * @param e the Emitter for the compile method
     */
    public void compile(Emitter e)
    {
        exp1.compile(e);
        e.emitPush("$v0");
        exp2.compile(e);
        e.emitPop("$t0");
        if (op.equals("+"))
            e.emit("addu $v0 $v0 $t0");
        else if (op.equals("-"))
            e.emit("subu $v0 $t0 $v0");
        else if (op.equals("*"))
        {
            e.emit("mult $t0 $v0");
            e.emit("mflo $v0");
        }
        else if (op.equals("/"))
        {
            e.emit("div $t0 $v0");
            e.emit("mflo $v0");
        }
    }
}
