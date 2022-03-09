package boazparser;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Recognizer for Boaz programs.
 *
 * @author djb
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.err.println("error");
        } else {
            String filename = args[0];
            if (filename.endsWith(".boaz")) {
                Parser parse = new Parser();
                parse.parse(filename);
            }
        }
    }
}
