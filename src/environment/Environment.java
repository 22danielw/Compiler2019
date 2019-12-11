package environment;

import ast.Expression;
import ast.ProcedureDeclaration;

import java.util.HashMap;

/**
 * Represents an Environment that stores a Hashmap of
 * variable names and their corresponding Integer values.
 * It can also map procedure names to declarations.
 *
 * @author Daniel Wu
 * @version 10/19/2019
 */
public class Environment
{
    HashMap<String, Integer> variables;
    HashMap<String, ProcedureDeclaration> procedures;
    Environment parent;

    /**
     * Constructs an environment by initializing the HashMap that
     * stores variable information.
     *
     * @param p the parent environment. If the Environment is the
     *          global environment, p will be null.
     */
    public Environment(Environment p)
    {
        variables = new HashMap<String, Integer>();
        procedures = new HashMap<String, ProcedureDeclaration>();
        parent = p;
    }

    /**
     * Sets a variable with the Hashmap's put method. This ensures
     * that setting a variable that has already been declared
     * overrides the previous value of the variable, while
     * setting a new variable will create a new key/value pair.
     *
     * @param variable the variable being stored
     * @param value the value of the stored variable
     */
    public void setVariable(String variable, int value)
    {
        if (!variables.containsKey(variable))
        {
            if (parent != null)
            {
                parent.setVariable(variable, value);
            }
            else
                declareVariable(variable, value);
        }
        else
            variables.put(variable, value);
    }


    /**
     * Declares a variable in the current environment by
     * adding the name and value into the variables Map.
     *
     * @param variable the name of the variable being declared
     * @param value the value of the variable being declared
     */
    public void declareVariable(String variable, int value)
    {
        variables.put(variable, value);
    }

    /**
     * Returns the value of a variable with a given name. If the current
     * environment does not contain the variable, the method looks into
     * the parent environments. If no environment contains the variable, the
     * method throws an Exception.
     *
     * @param name the name of the variable whose value is to be returned
     * @return the int value of the variable with the name name
     * @throws Exception if the variable is not found
     */
    public int getVariable(String name) throws Exception
    {
        if (variables.containsKey(name))
            return variables.get(name);
        if (parent != null)
            return parent.getVariable(name);
        throw new Exception("Variable has not been declared");
    }

    /**
     * Takes in a ProcedureDeclaration and stores it in the HashMap
     * procedures using the name as the key and the ProcedureDeclaration
     * object as the value. This method sets the ProcedureDeclaration
     * into the global environment.
     *
     * @param name the name of the ProcedureDeclaration
     * @param declaration the ProcedureDeclaration being stored
     */
    public void setProcedure(String name, ProcedureDeclaration declaration)
    {
        while (parent != null)
        {
            parent.setProcedure(name, declaration);
        }
        procedures.put(name, declaration);
    }

    /**
     * Returns a ProcedureDeclaration matching the given
     * name. It searches in the global environment and returns
     * the corresponding procedure.
     *
     * @param name the name of the procedure to be found
     * @return the ProcedureDeclaration with the corresponding name
     *         from the global environment
     */
    public ProcedureDeclaration getProcedure(String name)
    {
        while (parent != null)
        {
            return parent.getProcedure(name);
        }
        return procedures.get(name);
    }
}
