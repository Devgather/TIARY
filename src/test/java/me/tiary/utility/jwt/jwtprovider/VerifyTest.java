package me.tiary.utility.jwt.jwtprovider;

import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import common.factory.utility.jwt.JwtProviderFactory;
import me.tiary.properties.jwt.JwtProperties;
import me.tiary.utility.jwt.JwtProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("[JwtProvider] verify")
class VerifyTest {
    private JwtProvider jwtProvider;

    @BeforeEach
    void beforeEach() {
        final JwtProperties properties = new TestTokenProperties("Test");

        jwtProvider = JwtProviderFactory.create(properties);
    }

    @Test
    @DisplayName("[Fail] algorithm is mismatch")
    void failIfAlgorithmIsMismatch() {
        // Given
        // Algorithm = HMAC512, Payload = { "data": "Test" }, Secret Key = Test
        final String token = "eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJkYXRhIjoiVGVzdCJ9.ehkf3FQVbKY4XFGiOdTHcL8rYmmzss8Q-3iSctozmefcAbzibfos-Ch_lydD9FKTN_LmJIVj4YunKi3VmnInUw";

        // When, Then
        assertThrows(AlgorithmMismatchException.class, () -> jwtProvider.verify(token));
    }

    @Test
    @DisplayName("[Fail] token is invalid")
    void failIfTokenIsInvalid() {
        // Given
        final String token = "a.b.c";

        // When, Then
        assertThrows(JWTDecodeException.class, () -> jwtProvider.verify(token));
    }

    @Test
    @DisplayName("[Fail] signature is invalid")
    void failIfSignatureIsInvalid() {
        // Given
        // Algorithm = HMAC256, Payload = { "data": "Test" }, Secret Key = Invalid Secret Key
        final String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJkYXRhIjoiVGVzdCJ9.8m2ipdRrtI-MVw6MS8IRff-uMG-mH70maH0tR-gPAW8";

        // When, Then
        assertThrows(SignatureVerificationException.class, () -> jwtProvider.verify(token));
    }

    @Test
    @DisplayName("[Fail] token has expired")
    void failIfTokenHasExpired() {
        // Given
        // Algorithm = HMAC256, Payload = { "data": "Test", "exp": 0 }, Secret Key = Test
        final String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJkYXRhIjoiVGVzdCIsImV4cCI6MH0.ZbhLANDWkjWbQwkoKPDv_Xi8kfObrtTE8Ow_6Hk5D1A";

        // When, Then
        assertThrows(TokenExpiredException.class, () -> jwtProvider.verify(token));
    }

    @Test
    @DisplayName("[Success] token is valid")
    void successIfTokenIsValid() {
        // Given
        // Algorithm = HMAC256, Payload = { "data": "Test" }, Secret Key = Test
        final String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJkYXRhIjoiVGVzdCJ9.PR4ORmk9jc92Tb97B495Q26XP5_eL9I-OD4Rqx2f5xs";

        // When
        final DecodedJWT result = jwtProvider.verify(token);

        // Then
        assertThat(result.getClaim("data").asString()).isEqualTo("Test");
    }

    static class TestTokenProperties extends JwtProperties {
        public TestTokenProperties(final String secretKey) {
            super(secretKey);
        }
    }
}