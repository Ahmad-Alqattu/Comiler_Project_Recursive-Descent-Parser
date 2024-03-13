package com.example.comiler_project;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.List;
/*
Ahmad luay alqatow
1193000
 */
public class MainUI extends Application {
    private File selectedFile;
    private TextArea tokenTextArea;

    public static void main(String[] args) {
        launch(args);
    }


    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Compiler Project");

        Button fileSelectButton = new Button("Select File");
        fileSelectButton.setOnAction(e -> {
            try {
                selectFile(primaryStage);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        tokenTextArea = new TextArea();
        tokenTextArea.setEditable(false);
        System.out.println("Ahmad luay alqatow\n" + "ID: 1193000");
        Button parseButton = new Button("Parse File");
        parseButton.setOnAction(e -> parseFile());

        VBox layout = new VBox(10);
        layout.setAlignment(Pos.CENTER); // Center alignment for all elements in VBox
        layout.getChildren().addAll(fileSelectButton, tokenTextArea, parseButton);

        Scene scene = new Scene(layout, 400, 300);
        primaryStage.setScene(scene);

        primaryStage.show();
    }


    private void selectFile(Stage primaryStage) throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Source Code File");
        selectedFile = fileChooser.showOpenDialog(primaryStage);

        if (selectedFile != null) {
            tokenTextArea.clear();
            List<Token> tokens = Tokenizer.tokenizeFile(selectedFile.getAbsolutePath());
            displayTokens(tokens);
        } else {
            tokenTextArea.setText("Selected File: No file chosen");
        }
    }

    private void displayTokens(List<Token> tokens) {
        StringBuilder tokenText = new StringBuilder();
        for (Token token : tokens) {
            tokenText.append(token.toString()).append("\n");
        }
        tokenTextArea.setText(tokenText.toString());
    }

    private void parseFile() {
        // You can implement your parsing logic here
        if (selectedFile != null) {
            try {
                List<Token> tokens= Tokenizer.tokenizeFile(selectedFile.getAbsolutePath());

                for (Token token : tokens) {
                    System.out.println(token.toString());
                }
                System.out.println("############################\n");

                Parser p =new Parser(tokens);
                p.parse();
            } catch (Exception e) {
                System.err.println("Error during parsing: " + e.getMessage());
            }
        } else {
            System.out.println("No file selected for parsing.");
        }
    }
}
