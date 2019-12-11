package scanner;
import java.io.*;



public class ScannerTester {

    public static void main(String[] args) throws FileNotFoundException, ScanErrorException {
        InputStream reader = new FileInputStream("scannerTestAdvanced.txt");
        Scanner scanner = new Scanner(reader);

        while (scanner.hasNext()) {
            System.out.println(scanner.nextToken());
        }
    }
}
