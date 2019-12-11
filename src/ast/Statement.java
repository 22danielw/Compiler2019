package ast;

import emitter.Emitter;

/**
 * An abstract class representing an Statement.
 *
 * @author Daniel Wu
 * @version 10/20/19
 */
public abstract class Statement
{

    /**
     * A method that serves as a reminder to implement the compile method
     * for Statement's subclasses.
     *
     * @param e the Emitter emitting the code to be compiled
     */
    public void compile(Emitter e)
    {
        throw new RuntimeException("Implement me!");
    }
}
