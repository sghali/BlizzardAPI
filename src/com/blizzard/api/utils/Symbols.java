package com.blizzard.api.utils;

/**
 * Symbols.java - This enum holds all the symbols used commonly over the entire
 * application get the value using Symbols.AND.getValue()
 * 
 * @version 1.0
 */
public enum Symbols {
    EQUAL("="), WHITE_SPACE(" "), COMMA(","), AND("and"), EMPTY(""), AMPERSAND(
            "&"), ESCAPE("\n"), FORWARDSLASH("/"), OPENCURLY("{"), CLOSECURLY(
            "}"), UNDERSCORE("_"), COLON(":"), SINGLEQUOTE("'"), DOUBLEQUOTE(
            "\""), QUESTIONMARK("?"), NULL("null"), FOR(" for "), PIPE("\\|");

    private String symbol;

    /**
     * Sets the value of the 'Symbol'.
     * 
     * @param c
     *            the value to set.
     */
    private Symbols(String c) {
        symbol = c;
    }

    /**
     * Gets the value of the 'symbol' field.
     */
    public String getSymbol() {
        return symbol;
    }

}
