package me.tiary.common.util.web;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class HttpRequestUtils {

    public static String getRequestUriWithQueryString(final HttpServletRequest request) {
        final String requestUri = request.getRequestURI();
        final String queryString = request.getQueryString();

        return (queryString == null) ? (requestUri) : (requestUri + '?' + queryString);
    }

}
