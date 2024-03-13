package com.example.comiler_project;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;
/*
recursive descent parser project
Ahmad luay alqatow
1193000
 */
public class MainConsole {
    private File selectedFile;

    public static void main(String[] args) {
        MainConsole app = new MainConsole();
        app.run();
    }

    private void run() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Ahmad luay alqatow\n" + "ID: 1193000");
        System.out.println("Enter the path of the file to parse:");
        String filePath = scanner.nextLine();
        selectedFile = new File(filePath);

        if (!selectedFile.exists()) {
            System.out.println("File not found: " + filePath);
            return;
        }

        try {
            List<Token> tokens = Tokenizer.tokenizeFile(selectedFile.getAbsolutePath());
            parseFile();
        } catch (IOException e) {
            System.err.println("Error reading the file: " + e.getMessage());
        }
    }



    private void parseFile() throws IOException {
        if (selectedFile != null) {
            List<Token> tokens= Tokenizer.tokenizeFile(selectedFile.getAbsolutePath());

            for (Token token : tokens) {
                System.out.println(token.toString());
            }
            System.out.println("############################\n");

            Parser p =new Parser(tokens);
            p.parse();
        } else {
            System.out.println("No file selected for parsing.");
        }
    }
}
