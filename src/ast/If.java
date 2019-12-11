package ast;

import emitter.Emitter;

/**
 * A class that represents an If statement with a Condition
 * and a Statement.
 * Extends the abstract Statement class.
 *
 * @author Daniel Wu
 * @version 10/19/2019
 */
public class If extends Statement
{
    private Condition condition;
    private Statement statement;

    /**
     * Constructor for the If class. Assigns a condition and statement
     * ot the object.
     *
     * @param c the Condition stored by the If object
     * @param s the Statement stored by the If object
     */
    public If(Condition c, Statement s)
    {
        condition = c;
        statement = s;
    }

    /**
     * A getter for the Condition stored by the If object.
     *
     * @return the instance field condition
     */
    public Condition getCondition()
    {
        return condition;
    }

    /**
     * A getter for the Statement stored by the If object.
     *
     * @return the instance field statement
     */
    public Statement getStatement()
    {
        return statement;
    }

    /**
     * Converts an If statement to MIPS assembly code.
     * Uses the opposite of the boolean condition.
     *
     * @param e the Emitter that emits MIPS assembly code
     */
    public void compile(Emitter e)
    {
        String label = "endif" + e.nextLabelID();
        condition.compile(e, label);
        statement.compile(e);
        e.emit(label + ":");
    }
}
