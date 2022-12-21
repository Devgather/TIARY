package me.tiary.utility.common;

public final class FileUtility {
    public static String getFileExtension(final String fileName) {
        try {
            return fileName.substring(fileName.lastIndexOf("."));
        } catch (final StringIndexOutOfBoundsException e) {
            return "";
        }
    }
}