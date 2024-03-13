package com.example.comiler_project;

import java.util.List;
import java.util.Stack;

public class Parser {
    private List<Token> tokenList;
    private Stack<String> procedureList= new Stack<>() ;
    private String moduleName;
    private Token lastToken;

    private Token currentToken;
    private Token getNextToken() {
        if (tokenList.isEmpty()) {
            return null;
        }
        lastToken =currentToken;
        return tokenList.remove(0);
    }

    private void error(String message) {
        System.err.println("Error at line " + currentToken.getLineNumber() + ": " + message +" after '"+ lastToken.getValue()+"' of Type: '"+lastToken.getType()+"' ");
        System.err.println("The Token causing the error: '" + currentToken.getValue()+"' as  '"+ currentToken.getType()+"' Type");

        System.exit(1);
    }
    public Parser(List<Token> tokens) {
        this.tokenList = tokens;
        this.currentToken = null;
    }

    public void parse() {
        currentToken = getNextToken();
        moduleDecl();
        if (currentToken.getValue().equals(".")) {
            currentToken = getNextToken();
            if (currentToken!=null){
                error("un allowed tokens after program end");

            }
            System.out.println("Complete Parse successful! ");
        } else {
            error("Expected '.' at the end of the program.");
        }

    }

    private void moduleDecl() {
        moduleHeading();
        declarations();
        procedureDecl();
        block();
        if (moduleName.equals(currentToken.getValue())) {
            currentToken = getNextToken();
            if (currentToken==null){
                System.err.println("Error: Expected '.' at the end of the program.");

                System.exit(1);

            }
        }
        else {
            error(" "+currentToken.getValue()+" is not identify module checkName. ");
        }
    }

    private void moduleHeading() {
        match("module");
        moduleName=currentToken.getValue();
        checkName();
        match(";");
    }

    private void block() {
        match("begin");
        stmtList();
        match("end");
    }

    private void declarations() {
        constDecl();
        varDecl();
    }

    private void constDecl() {
        if (check("const")) {
            match("const");
            constList();
        }
    }

    private void constList() {
        while (checkType(TokenType.USER_DEFINED_NAME)) {
            checkName();
            match("=");
            value();
            match(";");
        }
    }

    private void varDecl() {
        if (check("var")) {
            match("var");
            varList();
        }
    }

    private void varList() {
        while (checkType(TokenType.USER_DEFINED_NAME)) {
            varItem();
            match(";");
        }
    }

    private void varItem() {
        checkNameList();
        match(":");
        dataType();
    }


    private void checkNameList() {
        checkName();
        while (check(",")) {
            match(",");
            checkName();
        }
    }

    private void dataType() {
        if (check("integer") || check("real") || check("char")) {
            currentToken = getNextToken();
        } else {
            error("Expected data type (integer, real, or char).");
        }
    }

    private void procedureDecl() {
        if (check("procedure")) {
            procedureHeading();
            declarations();
            block();
            if (procedureList.contains(currentToken.getValue())) {
                currentToken = getNextToken();
            }
            else {
                error(" "+currentToken.getValue()+" is not a procedure checkName.");
            }
            match(";");
        }
    }

    private void procedureHeading() {
        match("procedure");
        if (checkType(TokenType.USER_DEFINED_NAME)) {
            procedureList.add(currentToken.getValue());
            currentToken = getNextToken();


        }else if(checkType(TokenType.RESERVED_WORD)){
            error("at line "+ currentToken.getLineNumber()+" "+currentToken.getValue()+"is Reserved word can't used as procedure checkName");

        }else {
            error(" "+currentToken.getLineNumber()+" is not a procedure checkName. at line " + currentToken.getLineNumber());
        }
        match(";");
    }

    private void stmtList() {
        statement();
        while (check(";")) {
            match(";");
            statement();
        }
    }

    private void statement() {
        if (checkType(TokenType.USER_DEFINED_NAME)) {
            assStmt();
        } else if (check("readint") || check("readreal") || check("readchar") || check("readln")) {
            readStmt();
        } else if (check("writeint") || check("writereal") || check("writechar") || check("writeln")) {
            writeStmt();

        } else if (check("if")) {
            ifStmt();

        } else if (check("while")) {
            whileStmt();

        } else if (check("loop")) {
            repeatStmt();

        } else if (check("exit")) {
            exitStmt();

        } else if (check("call")) {
            callStmt();
        }

    }

    private void assStmt() {
        checkName();
        match(":=");


        exp();
    }

    private void exp() {
        term();
        while (check("+") || check("-")) {
            addOper();
            term();
        }
    }

    private void addOper() {
        if (check("+") || check("-")) {
            currentToken = getNextToken();
        } else {
            error("Expected '+' or '-'.");
        }
    }

    private void term() {
        factor();
        while (checkType(TokenType.MUL_OPER)) {
            mulOper();
            factor();
        }
    }

    private void mulOper() {
        if (checkType(TokenType.MUL_OPER)) {
            currentToken = getNextToken();
        } else {
            error("Expected '*', '/', 'mod', or 'div'.");
        }
    }

    private void factor() {
        if (check("(")) {
            match("(");
            exp();
            match(")");
        } else if (checkType(TokenType.USER_DEFINED_NAME)) {
            checkName();
        } else if (checkType(TokenType.INTEGER_VALUE) || checkType(TokenType.REAL_VALUE)) {
            value();
        } else {
            error("Expected '(' or '" +
                    "', or 'value'.");
        }
    }

    private void value() {
        if (checkType(TokenType.INTEGER_VALUE) || checkType(TokenType.REAL_VALUE)) {
            currentToken = getNextToken();
        } else {
            error("Expected 'integer-value' or 'real-value'.");
        }
    }

    private void readStmt() {
        if (check("readint") || check("readreal") || check("readchar")  ) {
            currentToken = getNextToken();
            match("(");
            checkNameList();
            match(")");
        } else if(check("readln")){
            currentToken = getNextToken();

        }else {
            error("Expected 'readint', 'readreal', 'readchar', or 'readln'.");
        }
    }

    private void writeStmt() {
        if (check("writeint") || check("writereal") || check("writechar") ) {
            currentToken = getNextToken();
            match("(");
            writeList();
            match(")");
        }  else if(check("writeln")){
        currentToken = getNextToken();

    }else {
            error("Expected 'writeint', 'writereal', 'writechar', or 'writeln'.");
        }
    }

    private void writeList() {
        writeItem();
        while (check(",")) {
            match(",");
            writeItem();
        }
    }

    private void writeItem() {
        if (checkType(TokenType.USER_DEFINED_NAME) || checkType(TokenType.REAL_VALUE) || checkType(TokenType.INTEGER_VALUE)) {
            currentToken = getNextToken();
        } else {
            error("Expected 'checkName' or 'value'.");
        }
    }

    private void ifStmt() {
        match("if");
        condition();
        match("then");
        stmtList();
        elseifPart();
        elsePart();
        match("end");
    }

    private void elseifPart() {
        while (check("elseif")) {
            match("elseif");
            condition();
            match("then");
            stmtList();
        }
    }

    private void elsePart() {
        if (check("else")) {
            match("else");
            stmtList();
        }
    }

    private void whileStmt() {
        match("while");
        condition();
        match("do");
        stmtList();
        match("end");
    }

    private void repeatStmt() {
        match("loop");
        stmtList();
        match("until");
        condition();
    }

    private void exitStmt() {
        match("exit");
    }

    private void callStmt() {
        match("call");
        if (procedureList.contains(currentToken.getValue())) {
            currentToken = getNextToken();
        }
        else {
            error(" "+currentToken.getValue()+" is not a procedure checkName.");
        }
    }

    private void condition() {
        checkNameValue();
        relationalOper();
        checkNameValue();
    }

    private void checkNameValue() {
        if ((checkType(TokenType.USER_DEFINED_NAME) || checkType(TokenType.REAL_VALUE) || checkType(TokenType.INTEGER_VALUE))) {
            currentToken = getNextToken();
        } else {
            error("Expected 'checkName' or 'value'.");
        }
    }

    private void relationalOper() {
        if (checkType(TokenType.RELATIONAL_OPER)) {
            currentToken = getNextToken();
        } else {
            error("Expected '=', '|=', '<', '<=', '>', '>='.");
        }
    }

    private void checkName() {
        if (checkType(TokenType.USER_DEFINED_NAME)) {
            currentToken = getNextToken();

        }else if(checkType(TokenType.RESERVED_WORD)){
            error(currentToken.getValue()+" is Reserved word can't used as variable");

        }else {
            error(currentToken.getLineNumber()+" is not a variable checkName ");
        }
    }



    private void match(String expected) {

        if (currentToken != null && currentToken.getValue().equals(expected)) {
            currentToken = getNextToken();
        } else {
            error("Expected token '" + expected +"'");
        }
    }



    private boolean check(String expected) {
        return currentToken != null && currentToken.getValue().equals(expected);
    }

    private boolean checkType(TokenType expected) {
        return currentToken != null && currentToken.getType().equals(expected);


    }


}
