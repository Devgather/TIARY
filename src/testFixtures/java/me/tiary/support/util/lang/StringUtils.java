package me.tiary.support.util.lang;

import java.security.SecureRandom;

public final class StringUtils {

    private static final SecureRandom random = new SecureRandom();

    public static String generateRandomString(final int length) {
        return random.ints('0', 'z' + 1)
                .filter(x -> CharUtils.isNumber((char) x) || CharUtils.isAlphabet((char) x))
                .limit(length)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

}
