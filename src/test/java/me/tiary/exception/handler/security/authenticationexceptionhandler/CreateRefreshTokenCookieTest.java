package me.tiary.exception.handler.security.authenticationexceptionhandler;

import common.factory.utility.jwt.JwtProviderFactory;
import me.tiary.exception.handler.security.AuthenticationExceptionHandler;
import me.tiary.properties.jwt.RefreshTokenProperties;
import me.tiary.utility.jwt.JwtProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.servlet.http.Cookie;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("[AuthenticationExceptionHandler] createRefreshTokenCookie")
class CreateRefreshTokenCookieTest {
    public static final String METHOD_NAME = "createRefreshTokenCookie";

    private AuthenticationExceptionHandler authenticationExceptionHandler;

    private Method createRefreshTokenCookieMethod;

    private JwtProvider refreshTokenProvider;

    @BeforeEach
    void beforeEach() throws Exception {
        refreshTokenProvider = JwtProviderFactory.createRefreshTokenProvider();

        authenticationExceptionHandler = new AuthenticationExceptionHandler(
                null, refreshTokenProvider, null
        );

        createRefreshTokenCookieMethod = authenticationExceptionHandler.getClass().getDeclaredMethod(
                METHOD_NAME, Map.class
        );

        createRefreshTokenCookieMethod.setAccessible(true);
    }

    @Test
    @DisplayName("[Fail] payload type is invalid")
    void failIfPayloadTypeIsInvalid() {
        // Given
        final Map<String, Short> payload = Map.of("key", (short) 0);

        // When, Then
        final InvocationTargetException result = assertThrows(InvocationTargetException.class, () -> createRefreshTokenCookieMethod.invoke(
                authenticationExceptionHandler, payload
        ));

        assertThat(result.getCause().getClass()).isEqualTo(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("[Success] refresh token cookie is created")
    void successIfRefreshTokenCookieIsCreated() throws Exception {
        // Given
        final Map<String, String> payload = Map.of("key", "value");

        // When
        final Cookie result = (Cookie) createRefreshTokenCookieMethod.invoke(authenticationExceptionHandler, payload);

        // Then
        assertThat(result.isHttpOnly()).isTrue();
        assertThat(result.getSecure()).isTrue();
        assertThat(result.getPath()).isEqualTo("/");
        assertThat(result.getMaxAge()).isEqualTo(refreshTokenProvider.getValidSeconds());
        assertThat(result.getName()).isEqualTo(RefreshTokenProperties.COOKIE_NAME);
        assertThat(result.getValue()).isNotNull();
    }
}