package ast;

import environment.Environment;
import jdk.jshell.spi.ExecutionControlProvider;

import java.util.ListIterator;

/**
 * An evaluator class that can execute statements or evaluate
 * expressions.
 *
 * @author Daniel Wu
 * @version 10/19/2019
 */
public class Evaluator
{

    /**
     * Executes a generic statement based on which subclass
     * the statement is. It then calls the method to execute which
     * executes the specific type of statement based
     * on which type of statement the statement parameter represents
     *
     *
     * @param statement the statement being executed
     * @param env the environment for the AST
     * @throws Exception when variable is not found
     */
    public void exec(Statement statement, Environment env) throws Exception
    {
        if (statement instanceof Writeln)
        {
            exec((Writeln) statement, env);
        }
        else if (statement instanceof Assignment)
        {
            exec((Assignment) statement, env);
        }
        else if (statement instanceof Block)
        {
            exec((Block) statement, env);
        }
        else if (statement instanceof If)
        {
            exec((If) statement, env);
        }
        else if (statement instanceof While)
        {
            exec((While) statement, env);
        }
        else if (statement instanceof ProcedureDeclaration)
        {
            exec((ProcedureDeclaration) statement, env);
        }
    }

    /**
     * Executes a Writeln statement by printing the value
     * of the expression stored in the Writeln object.
     *
     * @param stmt the Writeln statement being executed
     * @param env the environment for the AST
     * @throws Exception when variable is not found
     */
    public void exec(Writeln stmt, Environment env) throws Exception
    {
        Expression expr = stmt.getExpression();
        if (expr instanceof Number)
        {
            System.out.println(((Number) expr).getValue());
        }
        else if (expr instanceof Variable)
        {
            System.out.println(env.getVariable((((Variable) expr).getName())));
        }
        else if (expr instanceof BinOp)
        {
            System.out.println(eval((BinOp) expr, env));
        }
        else if (expr instanceof ProcedureCall)
        {
            System.out.println(eval((ProcedureCall) expr, env));
        }
    }

    /**
     * Executes a Assignment statement by assigning the
     * expression's value stored in the assignment object
     * into the Environment.
     *
     * @param assignment the Assignment statement being executed
     * @param env the environment for the AST
     * @throws Exception when variable is not found
     */
    public void exec(Assignment assignment, Environment env) throws Exception
    {
        Expression expr = assignment.getExpression();
        if (expr instanceof Number)
        {
            env.setVariable(assignment.getVariable(), ((Number) expr).getValue());
            return;
        }
        else if (expr instanceof Variable)
        {
            env.setVariable(assignment.getVariable(), env.getVariable(((Variable) expr).getName()));
        }
        else if (expr instanceof BinOp)
        {
            env.setVariable(assignment.getVariable(), eval((BinOp) expr, env));
        }
        else if (expr instanceof ProcedureCall)
        {
            env.setVariable(assignment.getVariable(), eval(((ProcedureCall) expr), env));
        }
    }

    /**
     * Executes a Block of statements by iterating through
     * the List of Statement classes inside the Block object.
     * It then executes each statement based on what type of
     * statement it is.
     *
     * @param block the Block of statements being executed
     * @param env the environment used by the AST
     * @throws Exception when variable is not found
     */
    public void exec(Block block, Environment env) throws Exception
    {
        ListIterator<Statement> statements = block.getStatements().listIterator();
        while (statements.hasNext())
        {
            Statement s = statements.next();
            if (s instanceof Assignment)
            {
                exec((Assignment) s, env);
            }
            else if (s instanceof Writeln)
            {
                exec((Writeln) s, env);
            }
            else if (s instanceof If)
            {
                exec((If) s, env);
            }
            else if (s instanceof While)
            {
                exec((While) s, env);
            }
            else if (s instanceof Block)
            {
                exec((Block) s, env);
            }
        }
    }

    /**
     * Executes an If statement by determining whether the Condition
     * evaluates to true, then executing the statement based on that result.
     *
     * @param ifStatement the If Statement being executed
     * @param env the environment used in the AST
     * @throws Exception when variable is not found
     */
    public void exec(If ifStatement, Environment env) throws Exception
    {
        boolean condition = eval(ifStatement.getCondition(), env);
        if (condition)
        {
            exec(ifStatement.getStatement(), env);
        }
    }

    /**
     * Executes a While statement by determining whether the Condition
     * evaluates to true, then executing the statement based on that result
     * in a while loop
     *
     * @param whileStatement the If Statement being executed
     * @param env the environment used in the AST
     * @throws Exception when variable is not found
     */
    public void exec(While whileStatement, Environment env) throws Exception
    {
        while (eval(whileStatement.getCondition(), env))
        {
            exec(whileStatement.getStatement(), env);
        }
    }

    /**
     * Executes a ProcedureDeclaration by setting the declaration into the
     * given environment and storing it to its name.
     *
     * @param procedureDec the ProcedureDeclaration being set/stored
     * @param env the environment to which the ProcedureDeclaration is stored to
     * @throws Exception when variable is not found
     */
    public void exec(ProcedureDeclaration procedureDec, Environment env)
    {
        env.setProcedure(procedureDec.getName(), procedureDec);
    }


    /**
     * Executes a full program by first executing the ProcedureDeclaration
     * statements, then executes a single statement (typically a BEGIN END block).
     *
     * @param prog the program being executed
     * @param env the environment within which the program is being evaluated
     * @throws Exception when variable is not found
     */
    public void exec(Program prog, Environment env) throws Exception
    {
        for (ProcedureDeclaration p: prog.getProcedures())
        {
            exec(p, env);
        }
        exec(prog.getStatement(), env);
    }

    /**
     * Evaluates an Expression that represents an integer. It does this
     * by calling the eval method of each specific expression object and
     * returning its value.
     *
     * @param expression the Expression being evaluated
     * @param env the environment used in the AST
     * @return the int value of the expression
     * @throws Exception when variable is not found
     */
    public int eval(Expression expression, Environment env) throws Exception
    {
        if (expression instanceof Number)
            return ((Number) expression).getValue();
        else if (expression instanceof Variable)
            return env.getVariable(((Variable) expression).getName());
        else if (expression instanceof BinOp)
            return eval((BinOp) expression, env);
        else if (expression instanceof ProcedureCall)
            return eval((ProcedureCall) expression, env);
        return 0;
    }

    /**
     * Evaluates a Condition and returns true or false based on the result.
     *
     * @precondition c contains an acceptable operator
     *
     * @param c the Condition being evaluated
     * @param env the environment used in the AST
     * @return true if the Condition c evaluates to true, false otherwise
     * @throws Exception when variable is not found
     */
    public boolean eval(Condition c, Environment env) throws Exception
    {
        String op = c.getOperator();
        Expression exp1 = c.getExpression1();
        Expression exp2 = c.getExpression2();
        int num1 = eval(exp1, env);
        int num2 = eval(exp2, env);
        if (op.equals("="))
            return num1 == num2;
        else if (op.equals("<>"))
            return num1 != num2;
        else if (op.equals("<"))
            return num1 < num2;
        else if (op.equals(">"))
            return num1 > num2;
        else if (op.equals("<="))
            return num1 <= num2;
        else if (op.equals(">="))
            return num1 >= num2;
        return false;
    }


    /**
     * Evaluates a binary operation represented by the BinOp object. It returns
     * the integer value of the operation.
     *
     * @param binop the BinOp being evaluated.
     * @param env the environment used in the AST
     * @return the int value represented by the BinOp object
     * @throws Exception when variable is not found
     */
    public int eval(BinOp binop, Environment env) throws Exception
    {
        Expression expr1 = binop.getExpression1();
        Expression expr2 = binop.getExpression2();
        int val1 = 0;
        if (expr1 instanceof Number)
            val1 = ((Number) expr1).getValue();
        else if (expr1 instanceof Variable)
            val1 = env.getVariable(((Variable) expr1).getName());
        else if (expr1 instanceof BinOp)
            val1 = eval((BinOp) expr1, env);

        int val2 = 0;
        if (expr2 instanceof Number)
            val2 = ((Number) expr2).getValue();
        else if (expr2 instanceof Variable)
            val2 = env.getVariable(((Variable) expr2).getName());
        else if (expr2 instanceof BinOp)
            val2 = eval((BinOp) expr2, env);
        if (binop.getOperator().equals("*"))
        {
            return val1 * val2;
        }
        else if (binop.getOperator().equals("/"))
        {
            return val1 / val2;
        }
        else if (binop.getOperator().equals("+"))
        {
            return val1 + val2;
        }
        else if (binop.getOperator().equals("-"))
        {
            return val1 - val2;
        }
        else if (binop.getOperator().equals("%"))
        {
            return val1 % val2;
        }
        return 0;
    }

    /**
     * Evaluates a Procedure Call from the user. First creates a hanging
     * environment from the global environment, then creates a variable in
     * said environment sharing the name of the procedure. This variable
     * stores the return value of the procedure. Then, the formal parameters
     * are replaced with actual values and the statement from the
     * ProcedureDeclaration is called. Finally, the value assigned to the
     * variable sharing the name of the procedure will be returned.
     *
     * @param call the ProcedureCall that is called by the user
     * @param env the global environment passed in for the procedure
     * @return the return value of the procedure; the value assigned to the
     *         variable sharing the name of the method in the
     *         local environment
     * @throws Exception when variable is not found
     */
    public int eval(ProcedureCall call, Environment env) throws Exception
    {
        ProcedureDeclaration dec = env.getProcedure(call.getName());
        ListIterator<Expression> iter = call.getArgs().listIterator();
        Environment localEnv = new Environment(env);
        localEnv.declareVariable(call.getName(), 0);

        int argIndex = 0;
        while (iter.hasNext())
        {
            Expression e = iter.next();
            localEnv.declareVariable(dec.getParams().get(argIndex), eval(e, env));
            argIndex++;
        }

        exec(dec.getDeclaration(), localEnv);
        return localEnv.getVariable(call.getName());
    }
}
