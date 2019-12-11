package ast;

import emitter.Emitter;

/**
 * A class that represents a While statement with a Condition
 * and a Statement.
 * Extends the abstract Statement class.
 *
 * @author Daniel Wu
 * @version 10/19/2019
 */
public class While extends Statement
{

    private Condition condition;
    private Statement statement;

    /**
     * Constructor for the While class. Assigns a condition and statement
     * ot the object.
     *
     * @param c the Condition stored by the While object
     * @param s the Statement stored by the While object
     */
    public While(Condition c, Statement s)
    {
        condition = c;
        statement = s;
    }

    /**
     * A getter for the Condition stored by the While object.
     *
     * @return the instance field condition
     */
    public Condition getCondition()
    {
        return condition;
    }

    /**
     * A getter for the Statement stored by the While object.
     *
     * @return the instance field statement
     */
    public Statement getStatement()
    {
        return statement;
    }

    /**
     * Emits the code for a loop. Compiles the statement
     * and condition, jumping to the a label each iteration
     * and breaking once the opposite of the condition is met.
     * Executes the statement block stored by the loop each iteration.
     *
     * @param e the Emitter emitting the code to be compiled
     */
    public void compile(Emitter e)
    {
        int labelID = e.nextLabelID();
        String startLabel = "loop" + labelID;
        String endLabel = "endloop" + labelID;

        e.emit(startLabel + ":");
        condition.compile(e, endLabel);
        statement.compile(e);
        e.emit("j " + startLabel);
        e.emit(endLabel  + ":");

    }
}
