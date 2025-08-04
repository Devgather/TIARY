package me.tiary.common.filter;

public final class AccessLoggingFilterHelper {

    public static final String LOG_FORMAT = AccessLoggingFilter.LOG_FORMAT.replace("{}", "%s");

    public static String generateAccessLog(final String remoteAddress, final String method, final String requestUri, final String protocol, final int status) {
        return String.format(LOG_FORMAT, remoteAddress, method, requestUri, protocol, status);
    }

}
