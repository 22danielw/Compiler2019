package ast;

import emitter.Emitter;

import java.util.List;

/**
 * A class that represents a block/group of Statements
 * by storing them in a list.
 * Extends the abstract Statement class.
 *
 * @author Daniel Wu
 * @version 10/19/2019
 */
public class Block extends Statement
{
    private List<Statement> stmts;

    /**
     * A constructor for a Block object. Instantiates the instance
     * field stmts which is a List<Statement> class.
     *
     * @param statements the list of statements to be assigned to the
     *                   instance field
     */
    public Block(List<Statement> statements)
    {
        stmts = statements;
    }

    /**
     * A getter that returns the List of statements stored
     * by the Block object
     *
     * @return the instance field stmts
     */
    public List<Statement> getStatements()
    {
        return stmts;
    }

    /**
     * Converts a block of statements to machine code.
     * Compiles each individual statement sequentially
     *
     * @param e the Emitter that emits code
     */
    public void compile(Emitter e)
    {
        for (Statement s: stmts)
        {
            s.compile(e);
        }
    }
}
