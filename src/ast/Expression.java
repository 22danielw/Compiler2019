package ast;

import emitter.Emitter;

/**
 * An abstract class representing an Expression.
 *
 * @author Daniel Wu
 * @version 10/20/19
 */
public abstract class Expression
{

    /**
     * Throws an error when executed as a reminder to implement
     * compile method in subclasses.
     *
     * @param e the Emitter that emits MIPS assembly code
     */
    public void compile(Emitter e)
    {
        throw new RuntimeException("Implement me!");
    }
}
