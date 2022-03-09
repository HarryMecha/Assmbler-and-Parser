//You are trying to fix the isExpression there needs to be a way to check which expression type it is without increasing cw maybe a counter of some sort
package boazparser;

import java.io.*;
import java.util.ArrayList;

public class Parser {

    public static int cw=0;
    //ArrayList<String> tokenizedFile//
    public void parse(String filename) throws IOException {
        Tokenizer token = new Tokenizer();
ArrayList<String>tokenizedFile= token.tokenize(filename);
        //System.out.println(tokenizedFile);
        if(isProgram(tokenizedFile)){
            System.out.println("ok");
        }else{
            System.out.println("error");
        }


    }

    public boolean isProgram(ArrayList tf) {
        if (tf.get(cw).equals("PROGRAM")) {
            cw++;
            if (tf.get(cw).equals("IDENTIFIER")) {
                cw++;
                if (isVarDecs(tf)) {
                    if(tf.get(cw).equals("BEGIN")){
                        cw++;
                        if(isStatements(tf)){
                            if(tf.get(cw).equals("END")){
                                return true;
                            }
                        }
                    }
                }
            }
        } return false;
    }

    public boolean isVarDecs(ArrayList tf){
            while(!checker(tf,cw).equals("BEGIN")) {
                if (isVarDec(tf)) {
                    if (checker(tf,cw).equals(";")){
                        cw++;
                    }
                } else {
                    return false;
                }
            }
        return true;
    }

    public boolean isVarDec(ArrayList tf){
        if(isType(tf)){
            cw++;
                    if(isVarList(tf)){
                        return true;
                    }

            } return false;
    }

    public boolean isType(ArrayList tf){

        if(checker(tf,cw).equals("INT")||checker(tf,cw).equals("CHAR")){
            return true;
        }
        return false;
    }

    public boolean isVarList(ArrayList tf){
        if(checker(tf,cw).equals("IDENTIFIER")){
            cw++;
            if(checker(tf,cw).equals(",")){
                cw++;
                if(isVarList(tf)){
                    return true;
                }
            } return true;
        }
        return false;
    }

    public boolean isStatements(ArrayList tf){
       while(checker(tf,cw).equals("IDENTIFIER")||
               checker(tf,cw).equals("IF")||
               checker(tf,cw).equals("PRINT")||
               checker(tf,cw).equals("WHILE")
       ) {
           if (isStatement(tf)) {
               cw++;
           } else {
               return false;
           }
       }
        return true;
    }

    public boolean isStatement(ArrayList tf){
        if(checker(tf,cw).equals("IDENTIFIER")) {
            if (isAssignStatement(tf)) {
                return true;
            }
        }
        if(checker(tf,cw).equals("IF")) {
            if (isIfStatement(tf)) {
                return true;
            }
        }
        if(checker(tf,cw).equals("PRINT")) {
            if (isPrintStatement(tf)) {
                return true;
            }

        }if(checker(tf,cw).equals("WHILE")) {
            if (isWhileStatement(tf)) {
                return true;
            }
        }
        return false;
        
        
    }

    public boolean isAssignStatement(ArrayList tf){
        if(checker(tf,cw).equals("IDENTIFIER")){
            cw++;

            if(checker(tf,cw).equals(":=")){
                cw++;
                if(isExpression(tf)){
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isIfStatement(ArrayList tf){
        if(checker(tf,cw).equals("IF")){
            cw++;
            if(isExpression(tf)){
                if(checker(tf,cw).equals("THEN")){
                    cw++;
                    if(isStatements(tf)){
                        if(checker(tf,cw).equals("ELSE")){
                            cw++;
                            if(isStatements(tf)){
                            }else return false;
                        }
                        if(checker(tf,cw).equals("FI")){
                            cw++;
                            return true;
                        }

                    }
                }
            }
        }
        return false;
    }

    public boolean isPrintStatement(ArrayList tf){
        if(checker(tf,cw).equals("PRINT")){
            cw++;
            if(isExpression(tf)){
                return true;
            }
        }
        return false;

    }

    public boolean isWhileStatement(ArrayList tf){
    if(checker(tf,cw).equals("WHILE")){
        cw++;
        if(isExpression(tf)){
            if(checker(tf,cw).equals("DO")){
                cw++;
                if(isStatements(tf)){
                    if(checker(tf,cw).equals("OD")){
                        cw++;
                        return true;
                    }
                }
            }
        }
    }return false;
    }

    public boolean isExpression(ArrayList tf){
        if(isTerm(tf)){
            while(isArithmeticOp(tf)
                    ||isBooleanOp(tf)
                    ||isRelationalOp(tf)) {
                if (isBinaryOp(tf)) {
                    if (isTerm(tf)) {
                    } else return false;
                }
                if (checker(tf, cw).equals(";")) {
                    return true;
                }
            }
            return true;
        }return false;
    }

    public boolean isBinaryOp(ArrayList tf){
        if(isArithmeticOp(tf)
                ||isBooleanOp(tf)
                ||isRelationalOp(tf)){
            cw++;
            return true;
        }return false;
    }

    public boolean isArithmeticOp(ArrayList tf){
    if(checker(tf,cw).equals("+")||
            checker(tf,cw).equals("-")||
            checker(tf,cw).equals("*")||
            checker(tf,cw).equals("/")){

        return true;
    }
        return false;

    }

    public boolean isBooleanOp(ArrayList tf){
        if(checker(tf,cw).equals("&")||
                checker(tf,cw).equals("|")){

            return true;
        }
        return false;

    }

    public boolean isRelationalOp(ArrayList tf){
        if(checker(tf,cw).equals("=")||
                checker(tf,cw).equals("!=")||
                checker(tf,cw).equals("<")||
                checker(tf,cw).equals(">")||
                checker(tf,cw).equals("<=")||
                checker(tf,cw).equals(">=")) {
            return true;
        }
        return false;

    }

    public boolean isTerm(ArrayList tf){

        if(checker(tf,cw).equals("INT_CONST")||
                checker(tf,cw).equals("CHAR_CONST")||
                checker(tf,cw).equals("IDENTIFIER")){
            cw++;
            return true;
        }

        if(checker(tf,cw).equals("-")||checker(tf,cw).equals("!")) {
            if (isUnaryOp(tf)) {
                cw++;
                if (isTerm(tf)) {

                    return true;
                }
                return false;
            }
        }
        cw--;
        if(checker(tf,cw).equals("(")){
            cw++;
            if(isExpression(tf)){
                if(checker(tf,cw).equals(")")){
                    return true;
                }
            }
        }
        return false;


    }

    public boolean isUnaryOp(ArrayList tf){
        if(checker(tf,cw).equals("-")||
                checker(tf,cw).equals("!")){
            return true;
        }return false;
    }

    public String checker(ArrayList tf, int cw){
        return tf.get(cw).toString();
    }

}
