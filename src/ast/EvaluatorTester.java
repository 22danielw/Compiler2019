package ast;

import environment.Environment;
import parser.Parser;
import scanner.Scanner;

import java.io.FileInputStream;
import java.io.InputStream;

public class EvaluatorTester {

    public static void main(String[] args) throws Exception
    {
        Evaluator e = new Evaluator();

        /*for (int i = 0; i < 5; i++) {
            String s = "parserTest" + i + ".txt";
            InputStream reader = new FileInputStream(s);
            Scanner scanner = new Scanner(reader);
            Parser p = new Parser(scanner);
            Environment env = new Environment();
            while (scanner.hasNext()) {
                e.exec(p.parseStatement(), env);
            }
        } */

        String s = "test";
        InputStream reader = new FileInputStream(s);
        Scanner scanner = new Scanner(reader);
        Parser p = new Parser(scanner);
        Environment env = new Environment(null);
        e.exec(p.parseProgram(), env);

    }
}
