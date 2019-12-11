package parser;

import ast.*;
import ast.Number;
import environment.Environment;
import scanner.ScanErrorException;
import scanner.Scanner;

import java.awt.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * A parser class that parses the input from a stream of
 * tokens from a scanner. It follows the grammar rules provided.
 * The file ends with a period.
 *
 * @author Daniel Wu
 *
 * @version 10/19/19
 */
public class Parser
{
    Scanner scanner;
    String currentToken;
    HashMap<String, Expression> variables;
    ArrayList<String> varDecs;

    /**
     * Constructs a Parser object from a Scanner and assigns
     * instance variables scanner and currentToken.
     *
     * @param s the Scanner that feeds tokens to the Parser
     * @throws ScanErrorException when there is an error in the Scanner
     */
    public Parser(Scanner s) throws ScanErrorException
    {
        scanner = s;
        currentToken = scanner.nextToken();
        variables = new HashMap<String, Expression>();
        varDecs = new ArrayList<String>();
    }

    /**
     * Eats a token and sets the currentToken the next token provided by
     * the scanner if the eaten token matches the previous currentToken.
     *
     * @precondition currentToken is valid and initialized
     * @param expected the expected currentToken
     * @throws ScanErrorException if the eaten token does not match the currentToken
     */
    private void eat(String expected) throws ScanErrorException
    {
        if (currentToken.equals(expected))
        {
            currentToken = scanner.nextToken();
        }
        else
        {
            throw new IllegalArgumentException(currentToken +
                    " was expected and " + expected + " was found.");
        }
    }

    /**
     * Parses an integer and returns its value. Eats the integer.
     *
     * @precondition currentToken is an integer
     * @postcondition number token has been eaten
     * @return the value of the parsed integer String
     * @throws ScanErrorException if eaten token does not match the currentToken
     */
    private Number parseNumber() throws ScanErrorException
    {
        int num = Integer.parseInt(currentToken);
        eat(currentToken);
        return new Number(num);
    }

    /**
     * Parses and returns a program. A Program contains any number of
     * Procedure Declarations, followed by a single statement (typically
     * a BEGIN END block). Returns a Program object.
     *
     * @return a Program object storing the ProcedureDeclarations and statement
     * @throws ScanErrorException if eaten token does not match currentToken
     */
    public Program parseProgram() throws ScanErrorException
    {
        ArrayList<String> vars = new ArrayList<String>();
        while (currentToken.equals("VAR"))
        {
            eat("VAR");
            String name = currentToken;
            eat(currentToken);
            while (!currentToken.equals(";"))
            {
                vars.add(name);
                eat(",");
                name = currentToken;
                eat(currentToken);
            }
            vars.add(name);
            eat(";");
        }
        varDecs = vars;

        //Parses all the procedure declarations of the program
        ArrayList<ProcedureDeclaration> decs = new ArrayList<ProcedureDeclaration>();
        while (currentToken.equals("PROCEDURE"))
        {
            eat("PROCEDURE");
            String name = currentToken;
            eat(currentToken);
            eat("(");
            ArrayList<String> params = new ArrayList<String>();
            while (!currentToken.equals(")"))
            {
                params.add(currentToken);
                eat(currentToken);
                if (currentToken.equals(","))
                    eat(",");
            }
            eat(")");
            eat(";");

            ArrayList<String> locals = new ArrayList<String>();
            while (currentToken.equals("VAR"))
            {
                eat("VAR");
                String localVar = currentToken;
                eat(currentToken);
                while (!currentToken.equals(";"))
                {
                    locals.add(localVar);
                    eat(",");
                    localVar = currentToken;
                    eat(currentToken);
                }
                locals.add(localVar);
                eat(";");
            }
            Statement s = parseStatement();
            decs.add(new ProcedureDeclaration(name, s, params, locals));
        }

        //returns a program with
        return new Program(vars, decs, parseStatement());
    }

    /**
     * The method parses a single statement based on the grammar for
     * parsing Statement.
     *
     * @return a new Statement class based on what kind of Statement the file contains
     * @throws ScanErrorException if eaten tokens do not match their currentToken
     */
    public Statement parseStatement() throws ScanErrorException
    {
        if (currentToken.equals("WRITELN"))
        {
            eat("WRITELN");
            eat("(");
            Expression exp = parseExpression();
            eat(")");
            eat(";");
            return new Writeln(exp);
        }
        else if (currentToken.equals("BEGIN"))
        {
            eat("BEGIN");
            Block ret = parseBegin(new Block(new ArrayList<Statement>()));
            return ret;
        }
        else if (currentToken.equals("IF"))
        {
            eat("IF");
            Condition c = parseCondition();
            eat("THEN");
            Statement s = parseStatement();
            Statement ifStatement = new If(c, s);
            return ifStatement;
        }
        else if (currentToken.equals("WHILE"))
        {
            eat("WHILE");
            Condition c = parseCondition();
            eat("DO");
            Statement s = parseStatement();
            Statement whileStatement = new While(c, s);
            return whileStatement;
        }
        else
        {
            String n = currentToken;
            eat(currentToken);
            eat(":=");
            Variable var = new Variable(n);
            Expression expr = parseExpression();
            variables.put(var.getName(), expr);
            eat(";");
            Statement assign = new Assignment(var.getName(), expr);
            return assign;
        }

    }

    /**
     * Parses a Begin statement that parses statements with the parseStatement
     * method until an END token is detected.
     *
     * @throws ScanErrorException if eaten tokens do not match their currentToken
     */
    private Block parseBegin(Block b) throws ScanErrorException
    {
        if (currentToken.equals("END"))
        {
            eat("END");
            eat(";");
            return b;
        }
        else
        {
            b.getStatements().add(parseStatement());
            return parseBegin(b);
        }
    }

    /**
     * Parses a factor as either an expr in parentheses, number,
     * identifier, or negative factor. This is based on the
     * grammar provided.
     *
     * @return the integer value of the factor given
     * @throws ScanErrorException if eaten tokens do not match their currentToken
     */
    private Expression parseFactor() throws ScanErrorException
    {
        if (currentToken.equals("(") || currentToken.equals(")"))
        {
            eat("(");
            Expression exp = parseExpression();
            eat(")");
            return exp;
        }
        else if (currentToken.equals("-"))
        {
            eat("-");
            return new BinOp("*", new Number(-1), parseFactor());
        }
        else if (scanner.isDigit(currentToken.charAt(0)))
            return parseNumber();
        else
        {
            String id = currentToken;
            eat(currentToken);
            if (currentToken.equals("("))
            {
                eat(currentToken);
                ArrayList<Expression> args = new ArrayList<>();
                while (!currentToken.equals(")"))
                {
                    args.add(parseExpression());
                    if (currentToken.equals(","))
                        eat(",");
                }
                eat(")");
                return new ProcedureCall(id, args);
            }

            return new Variable(id);
        }
    }

    /**
     * Parses a term as either a term * factor, term / factor,
     * or factor based on the grammar provided.
     *
     * @return the value of the term parsed based on grammars and
     *         evaluation rules
     * @throws ScanErrorException if eaten tokens do not match their currentToken
     */
    private Expression parseTerm() throws ScanErrorException
    {
        Expression exp = parseFactor();

        while (currentToken.equals("*") || currentToken.equals("/"))
        {
            if (currentToken.equals("*"))
            {
                eat("*");
                exp = new BinOp("*", exp, parseFactor());
            }
            else if (currentToken.equals("/"))
            {
                eat("/");
                exp = new BinOp("/", exp, parseFactor());
            }
        }
        return exp;
    }

    /**
     * Parses an expression by the rules expr + term, expr - term, and term
     * based on the grammar. Evaluates and returns the actual values.
     *
     * @return the int value of an expression
     * @throws ScanErrorException if eaten tokens do not match their currentToken
     */
    private Expression parseExpression() throws ScanErrorException
    {
        Expression exp = parseTerm();

        while (currentToken.equals("+") || currentToken.equals("-"))
        {
            if (currentToken.equals("+"))
            {
                eat("+");
                exp = new BinOp("+", exp, parseTerm());
            }
            else if (currentToken.equals("-"))
            {
                eat("-");
                exp = new BinOp("-", exp, parseTerm());
            }
        }
        return exp;
    }

    /**
     * Parses a Condition statement that includes two Expressions and
     * a relational operator based on the provided grammar.
     *
     * @return a Condition object from the information from the file
     * @throws ScanErrorException if eaten tokens do not match their currentToken
     */
    private Condition parseCondition() throws ScanErrorException
    {
        Expression e1 = parseExpression();
        String op = parseRelop();
        Expression e2 = parseExpression();
        return new Condition(op, e1, e2);
    }

    /**
     * Parses a Relational Operator and returns the result as a String.
     *
     * @return the relational operator found as a String
     * @throws ScanErrorException if eaten tokens do not match their currentToken
     */
    private String parseRelop() throws ScanErrorException
    {
        String op = currentToken;
        if (op.equals("=") || op.equals("<>") || op.equals("<") ||
                op.equals(">") || op.equals("<=") || op.equals(">="))
        {
            eat(currentToken);
            return op;
        }
        throw new ScanErrorException("Unexpected Token: RelOperator expected.");
    }

}