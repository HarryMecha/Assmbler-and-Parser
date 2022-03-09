package assembler;

import java.io.IOException;

/**
 * Driver program for Shack ASM to Hack ASM.
 * @author djb
 * @version 2021.12.04
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        if(args.length != 1) {
            System.err.println("Usage: sham file.shk");
        }
        else {
            String filename = args[0];
            if(filename.endsWith(".shk")) {
                try {
                    Assembler assem = new Assembler();
                    assem.assemble(filename);
                } catch (IOException ex) {
                    System.err.println("Unable to read " + filename);
                }
            }
            else {
                System.err.println("Unrecognised input");
            }
        }
    }
    
}
