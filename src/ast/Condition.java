package ast;

import emitter.Emitter;

/**
 * A class that represents a Conditional statement in
 * a program.
 * Extends the abstract Expression class.
 *
 * @author Daniel Wu
 * @version 10/19/2019
 */
public class Condition extends Expression
{
    private String operator;
    private Expression expr1;
    private Expression expr2;

    /**
     * A constructor for a Condition object. The condition includes
     * an operator and two expressions.
     *
     * @param op the operator in the Condition
     * @param e1 the first expression
     * @param e2 the second expression
     */
    public Condition(String op, Expression e1, Expression e2)
    {
        operator = op;
        expr1 = e1;
        expr2 = e2;
    }

    /**
     * A getter that returns the operator for the Condition class
     * as a String.
     *
     * @return the instance field operator
     */
    public String getOperator()
    {
        return operator;
    }

    /**
     * A getter that returns the first expression
     * stored by the Condition object
     *
     * @return the instance field expr1
     */
    public Expression getExpression1()
    {
        return expr1;
    }

    /**
     * A getter that returns the second expression
     * stored by the Condition object
     *
     * @return the instance field expr2
     */
    public Expression getExpression2()
    {
        return expr2;
    }

    /**
     * Converts the Condition class to MIPS assembly code. Reverses
     * the binary operator and jumps to a given label.
     *
     * @param e the Emitter that emits MIPS assembly code
     * @param label the label that the condition jumps to
     */
    public void compile(Emitter e, String label)
    {
        expr1.compile(e);
        e.emit("move $t0 $v0");
        expr2.compile(e);
        e.emit("move $t1 $v0");
        if (operator.equals("<="))
            e.emit("bgt $t0 $t1 " + label);
        else if (operator.equals("<"))
            e.emit("bge $t0 $t1 " + label);
        else if (operator.equals(">="))
            e.emit("blt $t0 $t1 " + label);
        else if (operator.equals(">"))
            e.emit("ble $t0 $t1 " + label);
        else if (operator.equals("="))
            e.emit("bne $t0 $t1 " + label);
        else if (operator.equals("<>"))
            e.emit("beq $t0 $t1 " + label);
    }
}
