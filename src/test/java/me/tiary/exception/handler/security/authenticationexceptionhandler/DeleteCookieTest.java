package me.tiary.exception.handler.security.authenticationexceptionhandler;

import me.tiary.exception.handler.security.AuthenticationExceptionHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.servlet.http.Cookie;
import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("[AuthenticationExceptionHandler] deleteCookie")
class DeleteCookieTest {
    public static final String METHOD_NAME = "deleteCookie";

    private AuthenticationExceptionHandler authenticationExceptionHandler;

    private Method deleteCookieMethod;

    @BeforeEach
    void beforeEach() throws Exception {
        authenticationExceptionHandler = new AuthenticationExceptionHandler(
                null, null, null
        );

        deleteCookieMethod = authenticationExceptionHandler.getClass().getDeclaredMethod(
                METHOD_NAME, String.class
        );

        deleteCookieMethod.setAccessible(true);
    }

    @Test
    @DisplayName("[Success] cookie is deleted")
    void successIfCookieIsDeleted() throws Exception {
        // When
        final Cookie result = (Cookie) deleteCookieMethod.invoke(
                authenticationExceptionHandler, "test"
        );

        // Then
        assertThat(result.isHttpOnly()).isTrue();
        assertThat(result.getSecure()).isTrue();
        assertThat(result.getPath()).isEqualTo("/");
        assertThat(result.getMaxAge()).isEqualTo(0);
        assertThat(result.getName()).isEqualTo("test");
        assertThat(result.getValue()).isNull();
    }
}