package me.tiary.exception.handler.security.authenticationexceptionhandler;

import common.factory.utility.jwt.JwtProviderFactory;
import me.tiary.exception.handler.security.AuthenticationExceptionHandler;
import me.tiary.utility.jwt.JwtProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.BadCredentialsException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("[AuthenticationExceptionHandler] checkAccessTokenExpiration")
class CheckAccessTokenExpirationTest {
    public static final String METHOD_NAME = "checkAccessTokenExpiration";

    private AuthenticationExceptionHandler authenticationExceptionHandler;

    private Method checkAccessTokenExpirationMethod;

    @BeforeEach
    void beforeEach() throws Exception {
        final JwtProvider accessTokenProvider = JwtProviderFactory.createAccessTokenProvider();

        authenticationExceptionHandler = new AuthenticationExceptionHandler(
                accessTokenProvider, null, null
        );

        checkAccessTokenExpirationMethod = authenticationExceptionHandler.getClass().getDeclaredMethod(
                METHOD_NAME, String.class
        );

        checkAccessTokenExpirationMethod.setAccessible(true);
    }

    @Test
    @DisplayName("[Fail] access token is null")
    void failIfAccessTokenIsNull() {
        // Given
        final String accessToken = null;

        // When, Then
        final InvocationTargetException result = assertThrows(InvocationTargetException.class, () -> checkAccessTokenExpirationMethod.invoke(
                authenticationExceptionHandler, accessToken
        ));

        assertThat(result.getCause().getClass()).isEqualTo(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("[Fail] access token is invalid")
    void failIfAccessTokenIsInvalid() {
        // Given
        final String accessToken = "a.b.c";

        // When, Then
        final InvocationTargetException result = assertThrows(InvocationTargetException.class, () -> checkAccessTokenExpirationMethod.invoke(
                authenticationExceptionHandler, accessToken
        ));

        assertThat(result.getCause().getClass()).isEqualTo(BadCredentialsException.class);
    }

    @Test
    @DisplayName("[Fail] access token algorithm is mismatch")
    void failIfAccessTokenAlgorithmIsMismatch() {
        // Given
        // Algorithm = HMAC512, Payload = { "uuid": "cbf0f220-97b8-4312-82ce-f98266c428d4" }, Secret Key = jwt-access-token-secret-key
        final String accessToken = "eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJ1dWlkIjoiY2JmMGYyMjAtOTdiOC00MzEyLTgyY2UtZjk4MjY2YzQyOGQ0In0.cCJL6etYw6r7tlXpuJqEQ7LccTOSsKbBW3LzavvqSvLPSJRBt8w7yMAB6d53Bs_FXf7YRqF5F9xrWyKMr7_KZw";

        // When, Then
        final InvocationTargetException result = assertThrows(InvocationTargetException.class, () -> checkAccessTokenExpirationMethod.invoke(
                authenticationExceptionHandler, accessToken
        ));

        assertThat(result.getCause().getClass()).isEqualTo(BadCredentialsException.class);
    }

    @Test
    @DisplayName("[Fail] access token signature is invalid")
    void failIfAccessTokenSignatureIsInvalid() {
        // Given
        // Algorithm = HMAC256, Payload = { "uuid": "cbf0f220-97b8-4312-82ce-f98266c428d4" }, Secret Key = invalid-secret-key
        final String accessToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1dWlkIjoiY2JmMGYyMjAtOTdiOC00MzEyLTgyY2UtZjk4MjY2YzQyOGQ0In0.ftqXO7VbB5rpAaJks-B9V2a43TmE23TOtTbVzzxAwg4";

        // When, Then
        final InvocationTargetException result = assertThrows(InvocationTargetException.class, () -> checkAccessTokenExpirationMethod.invoke(
                authenticationExceptionHandler, accessToken
        ));

        assertThat(result.getCause().getClass()).isEqualTo(BadCredentialsException.class);
    }

    @Test
    @DisplayName("[Success] access token is valid")
    void successIfAccessTokenIsValid() throws Exception {
        // Given
        // Algorithm = HMAC256, Payload = { "uuid": "cbf0f220-97b8-4312-82ce-f98266c428d4" }, Secret Key = jwt-access-token-secret-key
        final String accessToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1dWlkIjoiY2JmMGYyMjAtOTdiOC00MzEyLTgyY2UtZjk4MjY2YzQyOGQ0In0.rftGC07wvthl89A-lHN4NzeP2gcVv9UxTTnST3Nhqz8";

        // When
        final boolean result = (boolean) checkAccessTokenExpirationMethod.invoke(
                authenticationExceptionHandler, accessToken
        );

        // Then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("[Success] access token has expired")
    void successIfAccessTokenHasExpired() throws Exception {
        // Given
        // Algorithm = HMAC256, Payload = { "uuid": "cbf0f220-97b8-4312-82ce-f98266c428d4", "exp": 0 }, Secret Key = jwt-access-token-secret-key
        final String accessToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1dWlkIjoiY2JmMGYyMjAtOTdiOC00MzEyLTgyY2UtZjk4MjY2YzQyOGQ0IiwiZXhwIjowfQ.3xL9qBUNtPop2bKoKloECcG0Pu-nCJC7tdE3tTXJ2fk";

        // When
        final boolean result = (boolean) checkAccessTokenExpirationMethod.invoke(
                authenticationExceptionHandler, accessToken
        );

        // Then
        assertThat(result).isTrue();
    }
}