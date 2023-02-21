package me.tiary.utility.common;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CookieUtility {
    public static Cookie createCookie(final String name, final String value) {
        final Cookie cookie = new Cookie(name, value);

        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");

        return cookie;
    }

    public static Cookie createCookie(final String name, final String value, final int maxAge) {
        final Cookie cookie = createCookie(name, value);

        cookie.setMaxAge(maxAge);

        return cookie;
    }

    public static void addCookie(final HttpServletResponse response,
                                 final Cookie cookie) {
        response.addCookie(cookie);
    }

    public static void addCookie(final HttpServletResponse response,
                                 final String name,
                                 final String value) {
        addCookie(response, createCookie(name, value));
    }

    public static void addCookie(final HttpServletResponse response,
                                 final String name,
                                 final String value,
                                 final int maxAge) {
        addCookie(response, createCookie(name, value, maxAge));
    }

    public static void deleteCookie(final HttpServletResponse response,
                                    final String name) {
        addCookie(response, createCookie(name, null, 0));
    }
}