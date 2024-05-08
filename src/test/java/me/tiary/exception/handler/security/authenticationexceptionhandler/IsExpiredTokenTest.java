package me.tiary.exception.handler.security.authenticationexceptionhandler;

import common.factory.utility.jwt.JwtProviderFactory;
import me.tiary.exception.handler.security.AuthenticationExceptionHandler;
import me.tiary.properties.jwt.JwtProperties;
import me.tiary.utility.jwt.JwtProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
@DisplayName("[AuthenticationExceptionHandler] isExpiredToken")
class IsExpiredTokenTest {
    public static final String METHOD_NAME = "isExpiredToken";

    @InjectMocks
    private AuthenticationExceptionHandler authenticationExceptionHandler;

    private Method isExpiredTokenMethod;

    private JwtProvider tokenProvider;

    @BeforeEach
    void beforeEach() throws Exception {
        isExpiredTokenMethod = authenticationExceptionHandler.getClass().getDeclaredMethod(
                METHOD_NAME, String.class, JwtProvider.class
        );

        isExpiredTokenMethod.setAccessible(true);

        tokenProvider = JwtProviderFactory.create(new TestTokenProperties("test", 0));
    }

    @ParameterizedTest
    @MethodSource("failIfArgumentIsNullArguments")
    @DisplayName("[Fail] argument is null")
    void failIfArgumentIsNull(final String token, final JwtProvider tokenProvider) {
        // When, Then
        assertThatThrownBy(() -> isExpiredTokenMethod.invoke(authenticationExceptionHandler, token, tokenProvider))
                .isInstanceOf(InvocationTargetException.class)
                .getCause()
                .isInstanceOf(IllegalArgumentException.class);
    }

    static Stream<Arguments> failIfArgumentIsNullArguments() {
        JwtProvider tokenProvider = JwtProviderFactory.create(new TestTokenProperties("test", 0));

        return Stream.of(
                Arguments.of(null, tokenProvider),
                Arguments.of("a.b.c", null),
                Arguments.of(null, null)
        );
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "a.b.c",
            // Algorithm = HMAC512, Payload = { "uuid": "cbf0f220-97b8-4312-82ce-f98266c428d4" }, Secret Key = test
            "eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJ1dWlkIjoiY2JmMGYyMjAtOTdiOC00MzEyLTgyY2UtZjk4MjY2YzQyOGQ0In0.EIrgHdmw7cH-1tqqFAqOnGClpplJ9BZ-vqWL4pKMAyrvck-HSODLNrXRFcMVCYtJnKwFCf3UlHkfzCk4_0iitw",
            // Algorithm = HMAC256, Payload = { "uuid": "cbf0f220-97b8-4312-82ce-f98266c428d4" }, Secret Key = invalid-secret-key
            "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1dWlkIjoiY2JmMGYyMjAtOTdiOC00MzEyLTgyY2UtZjk4MjY2YzQyOGQ0In0.ftqXO7VbB5rpAaJks-B9V2a43TmE23TOtTbVzzxAwg4"
    })
    @DisplayName("[Fail] token is invalid")
    void failIfTokenIsInvalid(final String token) {
        // When, Then
        assertThatThrownBy(() -> isExpiredTokenMethod.invoke(authenticationExceptionHandler, token, tokenProvider))
                .isInstanceOf(InvocationTargetException.class)
                .getCause()
                .isInstanceOf(BadCredentialsException.class);
    }

    @Test
    @DisplayName("[Success] token is valid")
    void successIfTokenIsValid() throws Exception {
        // Given
        // Algorithm = HMAC256, Payload = { "uuid": "cbf0f220-97b8-4312-82ce-f98266c428d4" }, Secret Key = test
        final String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1dWlkIjoiY2JmMGYyMjAtOTdiOC00MzEyLTgyY2UtZjk4MjY2YzQyOGQ0In0.94ApTruN3e5pKkk1Z5P8WCDNXgNRC4bUk4VUrjKrDXo";

        // When
        final boolean result = (boolean) isExpiredTokenMethod.invoke(
                authenticationExceptionHandler, token, tokenProvider
        );

        // Then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("[Success] token has expired")
    void successIfTokenHasExpired() throws Exception {
        // Given
        // Algorithm = HMAC256, Payload = { "uuid": "cbf0f220-97b8-4312-82ce-f98266c428d4", "exp": 0 }, Secret Key = test
        final String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1dWlkIjoiY2JmMGYyMjAtOTdiOC00MzEyLTgyY2UtZjk4MjY2YzQyOGQ0IiwiZXhwIjowfQ.n2rEBGoqsZwlF363wULihixIueaINthWRmD8l7k0ZYM";

        // When
        final boolean result = (boolean) isExpiredTokenMethod.invoke(
                authenticationExceptionHandler, token, tokenProvider
        );

        // Then
        assertThat(result).isTrue();
    }

    private final static class TestTokenProperties extends JwtProperties {
        public TestTokenProperties(String secretKey, int validSeconds) {
            super(secretKey, validSeconds);
        }
    }
}