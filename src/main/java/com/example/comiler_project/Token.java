package com.example.comiler_project;

class Token {
    private final String value;
    private final TokenType type;
    private final int lineNumber;

    public Token(String value, TokenType type, int lineNumber) {
        this.value = value;
        this.type = type;
        this.lineNumber = lineNumber;
    }

    public String getValue() {
        return value;
    }

    public TokenType getType() {
        return type;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    @Override
    public String toString() {
        return "Token value='" + value + '\'' +
                ", type=" + type +
                ", line Number=" + lineNumber +
                '}';
    }
}