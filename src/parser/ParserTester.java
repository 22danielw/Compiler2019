package parser;
import scanner.Scanner;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.File;

public class ParserTester {

    public static void main(String[] args) throws Exception {
        for (int i = 0; i < 5; i++) {
            String s = "parserTest" + i + ".txt";
            InputStream reader = new FileInputStream(s);
            Scanner scanner = new Scanner(reader);
            Parser p = new Parser(scanner);
            while (scanner.hasNext())
                p.parseStatement();
        }
    }

}
