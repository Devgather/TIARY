package me.tiary.common.util.http;

import jakarta.servlet.http.HttpServletRequest;

public final class HttpRequestUtils {

    public static String getRequestUriWithQueryString(final HttpServletRequest request) {
        final String requestUri = request.getRequestURI();
        final String queryString = request.getQueryString();

        return (queryString == null) ? (requestUri) : (requestUri + '?' + queryString);
    }

}
