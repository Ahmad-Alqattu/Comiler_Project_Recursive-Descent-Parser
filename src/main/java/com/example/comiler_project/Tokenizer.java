package com.example.comiler_project;

import java.io.*;
import java.util.*;

public class Tokenizer {
    private static final List<String> bychar = Arrays.asList(":=", "|=", "<=", ">=","=<","=>");


    private static final Set<String> RESERVED_WORDS = new HashSet<>(Arrays.asList
            ("module", "begin", "end", "const", "var", "integer", "real",
                    "char", "procedure", "if", "elseif", "else", "while",
                    "loop", "until", "exit", "call", "readint", "readreal",
                    "readchar", "readln", "writeint", "writereal",
                    "writechar", "writeln"));
    private static final Set<String> SPECIAL_CHARACTERS = new HashSet<>(Arrays.asList(".", ";", ",", "(", ")", "+", "-", "*", "/", ":=", "|=", "<", "<=", ">", ">=","=", ":"));



    static ArrayList<Token> tokenizeFile(String filePath) throws IOException {
        ArrayList<Token> tokens = new ArrayList<>();
        Scanner scanner = new Scanner(new File(filePath));

        int lineNumber = 0;
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            line = line.replaceAll("([a-zA-Z]+)\\.", "$1 .");

            lineNumber++;

            Scanner lineScanner = new Scanner(line);
            lineScanner.useDelimiter("\\s+|(?<=[^a-zA-Z0-9_.])|(?=[^a-zA-Z0-9_.])");
            String temp = null;
            String tokenValue="";
            Token t = null;
            while (lineScanner.hasNext()) {
               String token= lineScanner.next();

                if(lineScanner.hasNext()&bychar.contains(temp= tokenValue+token.trim()))
                {
                    tokens.remove(t);
                tokenValue=temp;
                }else{
                    tokenValue=token;

                }


                TokenType tokenType = getTokenType(tokenValue);
                 t = new Token(tokenValue, tokenType, lineNumber);
                tokens.add(t);
            }
        }

        return tokens;
    }



    private static TokenType getTokenType(String token) {
        if (RESERVED_WORDS.contains(token)) {
            return TokenType.RESERVED_WORD;

        }  else if (token.matches("[0-9]+")) {
            return TokenType.INTEGER_VALUE;
        } else if (token.matches("[0-9]+\\.[0-9]+")) {
            return TokenType.REAL_VALUE;

        } else if (token.matches("=|\\|=|<|<=|>|>=")) {
            return TokenType.RELATIONAL_OPER;
        } else if (token.matches("\\+|-")) {
            return TokenType.ADD_OPER;
        } else if (token.matches("\\*|/|mod|div")) {
            return TokenType.MUL_OPER;
        }else if (token.matches("[a-zA-Z][a-zA-Z0-9]*")) {
            return TokenType.USER_DEFINED_NAME;
        } else if (SPECIAL_CHARACTERS.contains(token)) {
            return TokenType.SPECIAL_CHARACTER; } else {
            return TokenType.Illegal_Token;
        }
    }
}



