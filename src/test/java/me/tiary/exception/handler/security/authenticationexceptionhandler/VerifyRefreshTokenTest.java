package me.tiary.exception.handler.security.authenticationexceptionhandler;

import com.auth0.jwt.interfaces.DecodedJWT;
import common.factory.utility.jwt.JwtProviderFactory;
import me.tiary.exception.handler.security.AuthenticationExceptionHandler;
import me.tiary.properties.jwt.RefreshTokenProperties;
import me.tiary.utility.jwt.JwtProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.security.authentication.BadCredentialsException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("[AuthenticationExceptionHandler] verifyRefreshToken")
class VerifyRefreshTokenTest {
    public static final String METHOD_NAME = "verifyRefreshToken";

    private AuthenticationExceptionHandler authenticationExceptionHandler;

    private Method verifyRefreshTokenMethod;

    @BeforeEach
    void beforeEach() throws Exception {
        final JwtProvider refreshTokenProvider = JwtProviderFactory.createRefreshTokenProvider();

        authenticationExceptionHandler = new AuthenticationExceptionHandler(
                null, refreshTokenProvider, null
        );

        verifyRefreshTokenMethod = authenticationExceptionHandler.getClass().getDeclaredMethod(
                METHOD_NAME, String.class
        );

        verifyRefreshTokenMethod.setAccessible(true);
    }

    @Test
    @DisplayName("[Fail] refresh token is null")
    void failIfRefreshTokenIsNull() {
        // Given
        final String refreshToken = null;

        // When, Then
        final InvocationTargetException result = assertThrows(InvocationTargetException.class, () -> verifyRefreshTokenMethod.invoke(
                authenticationExceptionHandler, refreshToken
        ));

        assertThat(result.getCause().getClass()).isEqualTo(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "a.b.c",
            // Algorithm = HMAC512, Payload = { "uuid": "cbf0f220-97b8-4312-82ce-f98266c428d4" }, Secret Key = jwt-refresh-token-secret-key
            "eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJ1dWlkIjoiY2JmMGYyMjAtOTdiOC00MzEyLTgyY2UtZjk4MjY2YzQyOGQ0In0.DrHqyiNUJIg8oLv57WBBtKy-XtGJv-a7UieEECdto_KjY4cuansru38qz5MSyTszY58SLDgUUy9ZmBcS81157A",
            // Algorithm = HMAC256, Payload = { "uuid": "cbf0f220-97b8-4312-82ce-f98266c428d4", "exp": 0 }, Secret Key = jwt-refresh-token-secret-key
            "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1dWlkIjoiY2JmMGYyMjAtOTdiOC00MzEyLTgyY2UtZjk4MjY2YzQyOGQ0IiwiZXhwIjowfQ.ASuQpq3PXwLf_o_DobvpBYEljhfdJ-XFA1y-lueKXRA",
            // Algorithm = HMAC256, Payload = { "uuid": "cbf0f220-97b8-4312-82ce-f98266c428d4" }, Secret Key = invalid-secret-key
            "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1dWlkIjoiY2JmMGYyMjAtOTdiOC00MzEyLTgyY2UtZjk4MjY2YzQyOGQ0In0.ftqXO7VbB5rpAaJks-B9V2a43TmE23TOtTbVzzxAwg4"
    })
    @DisplayName("[Fail] refresh token is invalid")
    void failIfRefreshTokenIsInvalid(final String refreshToken) {
        // When, Then
        final InvocationTargetException result = assertThrows(InvocationTargetException.class, () -> verifyRefreshTokenMethod.invoke(
                authenticationExceptionHandler, refreshToken
        ));

        assertThat(result.getCause().getClass()).isEqualTo(BadCredentialsException.class);
    }

    @Test
    @DisplayName("[Success] refresh token is valid")
    void successIfRefreshTokenIsValid() throws Exception {
        // Given
        // Algorithm = HMAC256, Payload = { "uuid": "cbf0f220-97b8-4312-82ce-f98266c428d4" }, Secret Key = jwt-refresh-token-secret-key
        final String refreshToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1dWlkIjoiY2JmMGYyMjAtOTdiOC00MzEyLTgyY2UtZjk4MjY2YzQyOGQ0In0.RZSj06Y-qNhHTCsajTW10duaZf7ACSYiPZHUF3jQoPw";

        // When
        final DecodedJWT result = (DecodedJWT) verifyRefreshTokenMethod.invoke(
                authenticationExceptionHandler, refreshToken
        );

        final String profileUuid = result.getClaim(RefreshTokenProperties.RefreshTokenClaim.PROFILE_UUID.getClaim()).asString();

        // Then
        assertThat(profileUuid).isEqualTo("cbf0f220-97b8-4312-82ce-f98266c428d4");
    }
}