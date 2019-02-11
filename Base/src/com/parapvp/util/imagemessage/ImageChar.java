/*
 * Decompiled with CFR 0_115.
 */
package com.parapvp.util.imagemessage;

public enum ImageChar {
    BLOCK('\u2588'),
    DARK_SHADE('\u2593'),
    MEDIUM_SHADE('\u2592'),
    LIGHT_SHADE('\u2591');
    
    private final char character;

    private ImageChar(char character) {
        this.character = character;
    }

    public char getChar() {
        return this.character;
    }
}

