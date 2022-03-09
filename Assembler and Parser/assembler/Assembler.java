package assembler;


import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Coordinate the translation of Hack assembly code to
 * Hack machine code.
 *
 * @author
 * @version
 */
public class Assembler {
    /**
     * Create an assembler.
     */
    public Assembler() {
    }

    public ArrayList<String> legalOpcode = new ArrayList<String>();

    /**
     * Translate the Hack asm file.
     *
     * @param filename The file to be translated.
     * @throws IOException on any input issue.
     */
    public void assemble(String filename)
            throws IOException {
        String file2write = filename.substring(0, filename.length() - 4);
        File shackFile = new File(filename);

        Reader reader = new FileReader(shackFile);
        BufferedReader shackReader = new BufferedReader(reader);
        boolean atCode = false;
        boolean atDec = false;
        String currentLine;
        ArrayList<String> decList = new ArrayList<String>();

        File asmFile = new File(file2write + ".asm");
        asmFile.delete();
        populateLegalOp(legalOpcode);
        ArrayList<String> legalJump = new ArrayList<String>();


        while ((currentLine = shackReader.readLine()) != null) {

            if (currentLine.equals(".dec")) {
                atDec = true;
                atCode = false;
            }

            if (currentLine.equals(".code")) {
                atCode = true;
                atDec = false;
            }

            if (atDec == true && currentLine != (".dec")) {
                String[] wordArray = currentLine.split(("[ ]{1,}"));
                decList.add(wordArray[wordArray.length-1]);
            }
            if (atCode == true && !(currentLine.equals(".code"))) {

                WriteTo("// " + currentLine, asmFile);
                String[] arr = currentLine.split("[~@*+%{}<>\\[\\]|\"\\_^]", 2);
                if (arr.length <= 1 ) {

                    String[] wordArray = currentLine.split(("[ ]{1,}"));
                    if (!legalOpcode.contains(wordArray[0])) {
                        if(!wordArray[0].contains((":"))) {
                            String invalidOp = wordArray[0];
                            wordArray = new String[5];
                            wordArray[0] = invalidOp;
                        }
                    }
                    switch (wordArray.length) {

                        case 1:
                            if(wordArray[0].contains(":")){
                                checkLoop(wordArray[0],legalJump,decList,asmFile);
                            }else {
                                checkZOp(wordArray, decList, asmFile);
                            }
                            break;
                        case 2:
                            if (String.valueOf(wordArray[0].charAt(wordArray[0].length() - 1)).equals(("D"))) {
                                checkOpCode(wordArray, decList, asmFile);

                            } else {

                                checkJump(wordArray, decList, asmFile);
                            }
                            break;
                        case 3:
                            if (String.valueOf(wordArray[0].charAt(wordArray[0].length() - 1)).equals(("D"))) {
                                checkLoad(wordArray, decList, asmFile);

                            } else {

                                checkStore(wordArray, decList, asmFile);
                            }
                            break;
                        default:
                            System.err.println("Illegal opcode: " + wordArray[0]);
                            break;

                    }
                }else System.err.println("Illegal character: "+arr[0].charAt(arr.length));
            }

        }
    }


    private void checkOpCode(String[] word, ArrayList decList, File file) {
        boolean isMemory = false;
        String[] valArray = word[1].split("(?!^)");
        String value = word[1];
        if(legalOpcode.contains(value)){
            System.err.println(value+" is an opcode and may not be used as a label.");
            return;
        }
        if (valArray[0].equals("#")) {
            value = value.substring(1, value.length());

            WriteTo(("@" + value), file);


        } else {
            isMemory = true;

                WriteTo(("@" + value), file);


        }
        String[] OpArray = word[0].split("(?!^)");
        String Operand = word[0];
        if (Operand.endsWith("D")) {
            Operand = Operand.substring(0, Operand.length() - 1);
            switch (Operand) {
                case ("ADD"):
                    if (isMemory) {

                        WriteTo("D = D + M", file);

                    } else {

                        WriteTo("D = D + A", file);

                    }
                    break;
                case ("AND"):
                    if (isMemory) {
                        WriteTo("D = D & M", file);
                    } else {
                        WriteTo("D = D & A", file);
                    }
                    break;

                case ("OR"):
                    if (isMemory) {
                        WriteTo("D = D | M", file);
                    } else {
                        WriteTo("D = D | A", file);
                    }
                    break;
                case ("SUB"):
                    if (isMemory) {
                        WriteTo("D = D - M", file);
                    } else {
                        WriteTo("D = D - A", file);
                    }
                    break;

            }
        } else {
            System.err.println("Incorrect number of operands for " + word[0]);
        }
        WriteTo("", file);
    }

    private void checkZOp(String[] word, ArrayList decList, File file) {
        switch (word[0]) {
            case ("INC"):
                WriteTo("D = D + 1", file);
                break;
            case ("DEC"):
                WriteTo("D = D - 1", file);
                break;

            case ("CLR"):
                WriteTo("D = 0", file);
                break;
            case ("NEG"):
                WriteTo("D = -D", file);
                break;
            case ("NOT"):
                WriteTo("D = !D", file);
                break;
            default:
                System.err.println("Incorrect number of operands for " + word[0]);
                break;

        }
        WriteTo("", file);
    }

    private void checkStore(String[] word, ArrayList decList, File file) {
        String memAddress = word[1];
        if(legalOpcode.contains(word[2])){
            System.err.println(word[2]+" is an opcode and may not be used as a label.");
            return;
        }
        if(word[0].equals("STO")) {
            if (memAddress.equals("A")) {
                WriteTo("D = A", file);
                memAddress = null;
            }

            WriteTo("@" + word[2], file);
            WriteTo("M = D", file);
        }else{
            System.err.println("Incorrect number of operands for " + word[0]);
        }
        WriteTo("", file);
    }

    private void checkLoad(String[] word, ArrayList decList, File file) {
        boolean isMemory = false;
        String[] valArray = word[2].split("(?!^)");
        String value = word[2];

        if(legalOpcode.contains(word[2])){
            System.err.println(word[2]+" is an opcode and may not be used as a label.");
            return;
        }

        if (valArray.length == 1) {
            switch (valArray[0]) {
                case ("A"):
                    WriteTo(("D = A"), file);
                    break;
                case ("D"):
                    WriteTo(("A = D"), file);
                    break;
            }
        } else {

            switch (word[1]) {

                case ("A"):
                    WriteTo(("@" + word[2]), file);

                    if (valArray[0].equals("#")) {
                        value = value.substring(1, value.length());

                        WriteTo(("@" + value), file);

                    } else {
                        isMemory = true;

                        if (decList.contains(word[1].toString())) {
                            WriteTo(("@" + value), file);
                        } else {

                            System.err.println("RAM label " + word[1] + " has not been declared.");
                            return;
                        }
                    }
                    break;

                case ("D"):
                    WriteTo(("D = A"), file);
                    WriteTo(("@R13"), file);
                    WriteTo(("M = D"), file);
                    if (valArray[0].equals("#")) {
                        value = value.substring(1, value.length());
                        WriteTo(("@" + value), file);
                        WriteTo(("D = A"), file);

                    } else {
                        isMemory = true;
                        //write out everything

                        WriteTo(("@" + value), file);
                        WriteTo(("D = M"), file);


                    }
                    WriteTo(("@R13"), file);
                    WriteTo(("A = M"), file);
                    break;

                default:
                    System.err.println("Incorrect number of operands for " + word[0]);
                    break;
            }
        }
        WriteTo("", file);
    }

    private void checkJump(String[] word, ArrayList decList, File file) {
        String[] valArray = word[1].split("(?!^)");
        String value = word[1];
        if(legalOpcode.contains(value)){
            System.err.println(value+" is an opcode and may not be used as a label.");
            return;
        }
        if (valArray[0].equals("#")||decList.contains(word[1])) {

            System.err.println("RAM label "+value+" has been used as a jump destination.");

            return;


        }else{
            WriteTo(("@" + value), file);
        }
        String[] OpArray = word[0].split("(?!^)");
        String Operand = word[0];
        if (Operand.startsWith("J")) {
            switch (Operand) {
                case ("JMP"):

                    WriteTo("0; JMP", file);

                    break;
                case ("JGT"):

                    WriteTo("D; JGT", file);

                    break;

                case ("JEQ"):

                    WriteTo("D; JEQ", file);

                    break;
                case ("JGE"):

                    WriteTo("D; JGE", file);

                    break;
                case ("JLT"):

                    WriteTo("D; JLT", file);

                    break;
                case ("JNE"):

                    WriteTo("D; JNE", file);

                    break;
                case ("JLE"):

                    WriteTo("D; JLE", file);

                    break;


            }
        } else {
            System.err.println("Incorrect number of operands for " + word[0]);
        }
        WriteTo("", file);
    }

    private void checkLoop(String word, ArrayList legalJump, ArrayList decList, File file){
        if(legalOpcode.contains(word)){
            System.err.println(word+" is an opcode and may not be used as a label.");
        }else {
            if (decList.contains(word)) {
                System.err.println("ROM label " + word + " has been defined as a RAM label.");
            } else {
                if (legalJump.contains(word)) {
                    System.err.println("ROM label " + word + " has been defined more than once.");

                } else {
                    word = word.substring(0, word.length() - 1);
                    WriteTo("(" + word + ")", file);

                }

            }

        }


    }


    private void WriteTo(String toWrite, File file) {
        try {
            FileOutputStream fOut = new FileOutputStream(file, true);
            OutputStreamWriter osw = new OutputStreamWriter(fOut);
            osw.write(toWrite + "\n");
            osw.flush();
            osw.close();
        } catch (IOException e) {
        }
    }


    private ArrayList<String> populateLegalOp(ArrayList<String> legalOpcode) {
        legalOpcode.add("ADDD");
        legalOpcode.add("ANDD");
        legalOpcode.add("ORD");
        legalOpcode.add("SUBD");
        legalOpcode.add("INC");
        legalOpcode.add("DEC");
        legalOpcode.add("CLR");
        legalOpcode.add("NEG");
        legalOpcode.add("NOT");
        legalOpcode.add("STO");
        legalOpcode.add("LOAD");
        legalOpcode.add("JMP");
        legalOpcode.add("JGT");
        legalOpcode.add("JEQ");
        legalOpcode.add("JGE");
        legalOpcode.add("JLT");
        legalOpcode.add("JNE");
        legalOpcode.add("JLE");
        return legalOpcode;
        }
    }



