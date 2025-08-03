package me.tiary.common.advice;

import java.util.List;

public final class GlobalExceptionHandlerHelper {

    public static final String LOG_FORMAT = GlobalExceptionHandler.LOG_FORMAT.replace("{}", "%s");

    public static String generateExceptionLog(final Exception exception, final String path, final List<String> messages) {
        final String exceptionName = exception.getClass().getName();

        return String.format(LOG_FORMAT, exceptionName, path, messages);
    }

}
