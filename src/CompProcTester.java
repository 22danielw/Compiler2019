import ast.Program;
import environment.Environment;
import parser.Parser;
import scanner.Scanner;

import java.io.FileInputStream;
import java.io.InputStream;

public class CompProcTester
{
    public static void main(String[] args) throws Exception
    {
        String s = "test";
        InputStream reader = new FileInputStream(s);
        Scanner scanner = new Scanner(reader);
        Parser p = new Parser(scanner);
        Environment env = new Environment(null);

        p.parseProgram().compile("emitted");
    }

}
