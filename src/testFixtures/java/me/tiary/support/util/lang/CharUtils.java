package me.tiary.support.util.lang;

public final class CharUtils {

    public static boolean isAlphabet(final char character) {
        return isLowerCaseAlphabet(character) || isUpperCaseAlphabet(character);
    }

    public static boolean isLowerCaseAlphabet(final char character) {
        return 'a' <= character && character <= 'z';
    }

    public static boolean isUpperCaseAlphabet(final char character) {
        return 'A' <= character && character <= 'Z';
    }

    public static boolean isNumber(final char character) {
        return '0' <= character && character <= '9';
    }

}
