package me.tiary.utility.common;

import java.util.Random;

public final class StringUtility {
    public static String generateRandomString(final int length) {
        final Random random = new Random();

        return random.ints('0', 'z' + 1)
                .filter(x -> (x <= '9' || x >= 'A') && (x <= 'Z' || x >= 'a'))
                .limit(length)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }
}