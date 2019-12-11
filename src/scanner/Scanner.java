package scanner;
import java.io.*;

/**
 * scanner is a simple scanner for Compilers and Interpreters (2014-2015)
 * lab exercise 1.
 *
 * @author Daniel Wu
 * @author Ms. Datar
 *  
 * Usage: This object is used to separate a document into
 * lexemes based on regular expressions for identifiers,
 * digits, and operators.
 *
 * @version 09/08/2019
 */
public class Scanner
{
    private BufferedReader in;
    private char currentChar;
    private boolean eof;


    /**
     * scanner constructor for construction of a scanner that
     * uses an InputStream object for input.  
     * Usage: 
     * FileInputStream inStream = new FileInputStream(new File(<file name>);
     * scanner lex = new scanner(inStream);
     *
     * @param inStream the input stream to use
     */
    public Scanner(InputStream inStream)
    {
        in = new BufferedReader(new InputStreamReader(inStream));
        eof = false;
        getNextChar();
    }


    /**
     * scanner constructor for constructing a scanner that
     * scans a given input string.  It sets the end-of-file flag an then reads
     * the first character of the input string into the instance field currentChar.
     * Usage: scanner lex = new scanner(input_string);
     *
     * @param inString the string to scan
     */
    public Scanner(String inString)
    {
        in = new BufferedReader(new StringReader(inString));
        eof = false;
        getNextChar();
    }

    /**
     * Method: getNextChar
     * Gets the next character from the input stream and sets it to
     * the currentChar. If the end of file is reached, the eof
     * boolean is set to true.
     *
     */
    private void getNextChar()
    {
        try
        {
            int tempChar = in.read();
            if (tempChar == -1)
            {
                eof = true;
            }
            else
                currentChar = (char) tempChar;
        }
        catch (IOException e)
        {
            e.printStackTrace();
            System.exit(-1);
        }

    }

    /**
     * Method: eat
     *
     * Eats the current character from the scanner and returns the next character
     *
     * @param expected the character that is expected to be eaten
     *
     * @return the next character from the input stream
     *
     * @throws ScanErrorException an exception that is thrown when an error with
     * the scanning occurs i.e. the eaten character does not match the current
     * character.
     *
     */
    private char eat(char expected) throws ScanErrorException
    {
        if (expected == currentChar)
        {
            getNextChar();
            return currentChar;
        }
        else
            throw (new
                    ScanErrorException("Illegal Character - expected " + currentChar +
                    " and found " + expected));
    }

    /**
     * Method: hasNext
     *
     * Returns whether or not the input stream has a next character
     *
     * @return whether true if the input stream has another character, false otherwise
     */
    public boolean hasNext()
    {
        return !eof;
    }

    /**
     * Returns whether or not the character is a digit
     *
     * @param character the character being processed
     * @return true if the character is a digit between 0-9, false otherwise
     */
    public static boolean isDigit(char character)
    {
        return character >= '0' && character <= '9';
    }

    /**
     * Returns whether or not the character is a letter
     *
     * @param character the character being processed
     * @return true if the character is in a capital or lowercase letter, false
     * otherwise
     */
    public static boolean isLetter(char character)
    {
        return character >= 'a' && character <= 'z' || character >= 'A' && character <= 'Z';
    }

    /**
     * Returns whether or not the character is a white space
     *
     * @param character the character being processed
     * @return true if the character is in a white space (tab, new line),
     * false otherwise
     */
    public static boolean isWhiteSpace(char character)
    {
        return character == ' ' || character == '\t' || character == '\n' || character == '\r';
    }

    /**
     * Returns whether or not the character is a special character
     *
     * @param character the character being processed
     * @return true if the character is in a special character ($, ^, @, ;, :),
     * false otherwise
     */
    public static boolean isSpecial(char character)
    {
        return character == '$' || character == '^' || character == '@' || character == ';' ||
                character == ':';
    }

    /**
     * Scans a number based on the regex (digit)(digit)*; if the regex is
     * not met, the method returns a blank String
     *
     * @return a String based on the regular expression (digit)(digit)*
     * or an empty String
     * @throws ScanErrorException An exception that is thrown when
     * the eat method does not eat the currentChar
     */
    private String scanNumber() throws ScanErrorException
    {
        String ret = "";
        if (isDigit(currentChar))
        {
            ret += currentChar;
            eat(currentChar);
            while (hasNext() && isDigit(currentChar))
            {
                ret+= currentChar;
                eat(currentChar);
            }
        }
        return ret;
    }

    /**
     * Scans an identifier based on the regex (letter)(letter | digit)*; if the regex is
     * not met, the method returns a blank String
     *
     * @return a String based on the regular expression (letter)(letter | digit)*
     * or an empty String
     * @throws ScanErrorException An exception that is thrown when
     * the eat method does not eat the currentChar
     */
    private String scanIdentifier() throws ScanErrorException
    {
        String ret = "";
        if (isLetter(currentChar))
        {
            ret += currentChar;
            eat(currentChar);
            while (hasNext() && (isDigit(currentChar) || isLetter(currentChar)))
            {
                ret+= currentChar;
                eat(currentChar);
            }
        }
        return ret;
    }

    /**
     * Scans an identifier based on the regex ( = | + | - | * | / | % | ( | ) | > | < | . );
     * if the regex is not met, the method returns a blank String
     *
     * @return a String based on the regular expression
     * ( = | + | - | * | / | % | ( | ) | > | < | . ) or an empty String
     * @throws ScanErrorException An exception that is thrown when
     * the eat method does not eat the currentChar
     */
    private String scanOperator() throws ScanErrorException
    {
        if (currentChar == '+' || currentChar == '-' ||
                currentChar == '*' || currentChar == '/' || currentChar == '%' ||
                currentChar == '(' || currentChar == ')' || currentChar == '.' ||
                currentChar == ',')
        {
            if (currentChar == '.')
            {
                eof = true;
                return ".";
            }
            char ret = currentChar;
            eat(currentChar);
            return Character.toString(ret);
        }
        else
            throw new ScanErrorException("Unrecognized character");
    }

    /**
     * Scans a valid relational operator defined by the grammar.
     *
     * @return a String of the relational operator
     * @throws ScanErrorException if eaten char does not match currentChar
     */
    private String scanRelOperator() throws ScanErrorException
    {
        if (currentChar == '=' || currentChar == '>' || currentChar == '<')
        {
            if (currentChar == '=')
            {
                eat(currentChar);
                return "=";
            }
            else if (currentChar == '>')
            {
                eat(currentChar);
                if (currentChar == '=')
                {
                    eat('=');
                    return ">=";
                }
                return ">";
            }
            else
            {
                eat(currentChar);
                if (currentChar == '>')
                {
                    eat(currentChar);
                    return "<>";
                }
                else if (currentChar == '=')
                {
                    eat(currentChar);
                    return "<=";
                }
                return "<";
            }
        }
        throw new ScanErrorException("RelOp Expected.");
    }

    /**
     * Returns whether or not the currentChar is the start of a
     * relational operator defined by the grammar.
     *
     * @return true if the currentChar could lead into a defined
     *         relational operator, false otherwise.
     */
    private boolean isRelOperator()
    {
        return currentChar == '=' || currentChar == '>' || currentChar == '<';
    }

    /**
     * Scans a special character based on the regex defined in the method isSpecial; if
     * the regex is not met, the method returns a blank String
     *
     * @return a String based on the regular expression from isSpecial()
     * or an empty String
     * @throws ScanErrorException An exception that is thrown when
     * the eat method does not eat the currentChar
     */
    private String scanSpecial() throws ScanErrorException
    {
        if (isSpecial(currentChar))
        {
            if (currentChar == ':')
            {
                eat(currentChar);
                if (currentChar == '=')
                {
                    eat('=');
                    return ":=";
                }
                return ":";
            }
            char ret = currentChar;
            eat(currentChar);
            return Character.toString(ret);
        }
        else
            throw new ScanErrorException("Unrecognized Special Character");
    }

    /**
     * Method: nextToken
     * Returns the next Token based on a regex defining an identifier, digit, operator, or
     * special character. Returns "END" if the stream is at the end of file.
     *
     * @return the next Token produced by the scanner based on the currentChar
     * @throws ScanErrorException when the eaten character does not match the currentChar
     */
    public String nextToken() throws ScanErrorException
    {
        if (eof)
            return "END";
        while (hasNext() && isWhiteSpace(currentChar))
        {
            eat(currentChar);
        }
        if (isDigit(currentChar))
            return scanNumber();
        else if (isLetter(currentChar))
            return scanIdentifier();
        else if (isSpecial(currentChar))
            return scanSpecial();
        else if (isRelOperator())
            return scanRelOperator();
        else if (eof)
            return "END";
        else
            return scanOperator();
    }
}
