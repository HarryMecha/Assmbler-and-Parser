package boazparser;

import java.io.*;
import java.util.ArrayList;

import java.util.Locale;
import java.util.Scanner;

public class Tokenizer {

  boolean validLine = true;

    private ArrayList<String>tokens = new ArrayList<>();
    private ArrayList<String>tokenizedFile = new ArrayList<>();
    private ArrayList<String>operators = new ArrayList<>();
    private int inc = 0;
  public Tokenizer() {

    }

    public ArrayList<String> getTokenizedFile() {
        return tokenizedFile;
    }

    public void addTokenizedFile(String toAdd) {
        tokenizedFile.add(toAdd);
    }

    public ArrayList<String> tokenize(String filename) throws IOException {

        File file2Read = new File(filename);
        Reader reader = new FileReader(file2Read);
        BufferedReader buffReader = new BufferedReader(reader);
        String currentLine;
        currentLine = "";
        setupOps();
        setupTokens();

            int lineInc = 0;
            String[] wordArray = currentLine.split(("[ -]{1,}"));


            while ((currentLine = buffReader.readLine()) != null) {
                wordArray = currentLine.split("[\\s\t]+");
                for (String word : wordArray) {
                    if(word != "") {
                        analyse(word);
                    }
                }



        }
        System.out.println(tokenizedFile);
            return tokenizedFile;

    }

    public void analyse(String toAnalyse){
      boolean endSemiC = false;
      boolean endComma = false;
      boolean brackets = false;
      if(toAnalyse.endsWith(";")){
          endSemiC = true;
          toAnalyse = toAnalyse.substring(0, (toAnalyse.length()-1));
      }
        if(toAnalyse.endsWith(",")){
            endComma = true;
            toAnalyse = toAnalyse.substring(0, (toAnalyse.length()-1));
        }
        if(toAnalyse.endsWith(")")&&toAnalyse.startsWith("(")){
            brackets = true;
            if(toAnalyse.length()>1)toAnalyse = toAnalyse.substring(0, (toAnalyse.length()-1));
        }
        if(toAnalyse.startsWith("(")){
            addTokenizedFile("(");
            if(toAnalyse.length()>1) toAnalyse = toAnalyse.substring(1, (toAnalyse.length()));

        }



        if(operators.contains(toAnalyse)){
            addTokenizedFile(toAnalyse);
        }
      else if (tokens.contains(toAnalyse)){
          addTokenizedFile(toAnalyse.toUpperCase(Locale.ROOT));
      }
      else{
          if((toAnalyse.startsWith("\"")&&toAnalyse.endsWith("\""))&&!(toAnalyse.length()>4)){
              addTokenizedFile("CHAR_CONST");
          }
          else if (toAnalyse.matches("[0-9]+")){
              addTokenizedFile("INT_CONST");
          } else {
              if(!toAnalyse.equals(""))
              addTokenizedFile("IDENTIFIER");
          }

        }



if(endSemiC == true){
    addTokenizedFile(";");
}

        if(endComma == true){
            addTokenizedFile(",");
        }
        if(brackets == true){
            addTokenizedFile(")");
        }

    }

    public void setupTokens(){
      tokens.add("program");
        tokens.add("begin");
        tokens.add("end");
        tokens.add("int");
        tokens.add("char");
        tokens.add("print");
        tokens.add("else");
        tokens.add("then");
        tokens.add("if");
        tokens.add("fi");
        tokens.add("while");
        tokens.add("do");
        tokens.add("od");
    }
    public void setupOps(){
        operators.add("+");
        operators.add("-");
        operators.add("/");
        operators.add("*");
        operators.add("/");
        operators.add("&");
        operators.add("|");
        operators.add("=");
        operators.add("!=");
        operators.add("<");
        operators.add(">");
        operators.add("<=");
        operators.add(">=");
        operators.add("!");
        operators.add(":=");
    }

    public boolean analyseOp(String toAnalyse){
        switch(toAnalyse){
            case("+"):
                return true;

            case("-"):
                return true;

            case("*"):
                return true;

            case("/"):
                return true;

            case("&"):
                return true;

            case("|"):
                return true;

            case("="):
                return true;

            case("!="):
                return true;

            case("<"):
                return true;

            case(">"):
                return true;

            case("<="):
                return true;

            case(">="):
                return true;

            case("!"):
                return true;

            case(":="):
                return true;


        }
return false;
    }

}