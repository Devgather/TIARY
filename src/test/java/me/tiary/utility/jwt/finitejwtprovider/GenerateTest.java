package me.tiary.utility.jwt.finitejwtprovider;

import com.auth0.jwt.interfaces.DecodedJWT;
import common.factory.utility.jwt.JwtProviderFactory;
import me.tiary.properties.jwt.FiniteJwtProperties;
import me.tiary.utility.jwt.FiniteJwtProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("[FiniteJwtProvider] generate")
class GenerateTest {
    private FiniteJwtProvider jwtProvider;

    private TestTokenProperties properties;

    @BeforeEach
    void beforeEach() {
        properties = new TestTokenProperties("Test", 300);

        jwtProvider = JwtProviderFactory.create(properties);
    }

    @Test
    @DisplayName("[Fail] payload type is invalid")
    void failIfPayloadTypeIsInvalid() {
        // Given
        final Map<String, Short> payload = new HashMap<>();
        payload.put("key", (short) 0);

        // When, Then
        assertThrows(IllegalArgumentException.class, () -> jwtProvider.generate(payload));
    }

    @Test
    @DisplayName("[Success] token is generated")
    void successIfTokenIsGenerated() {
        // Given
        final Map<String, String> payload = new HashMap<>();
        payload.put("key", "value");

        // When
        final Date startDate = new Date();
        final String token = jwtProvider.generate(payload);
        final Date endDate = new Date();

        final DecodedJWT result = jwtProvider.verify(token);

        final Date expirationDate = result.getExpiresAt();
        final long elapsedSecondsFromStartToExpiration = expirationDate.getTime() / 1000 - startDate.getTime() / 1000;
        final long elapsedSecondsFromEndToExpiration = expirationDate.getTime() / 1000 - endDate.getTime() / 1000;

        // Then
        assertThat(result.getClaim("key").asString()).isEqualTo(payload.get("key"));
        assertThat(elapsedSecondsFromStartToExpiration).isGreaterThanOrEqualTo(properties.getValidSeconds());
        assertThat(elapsedSecondsFromEndToExpiration).isLessThanOrEqualTo(properties.getValidSeconds());
    }

    static class TestTokenProperties extends FiniteJwtProperties {
        public TestTokenProperties(final String secretKey, final int validSeconds) {
            super(secretKey, validSeconds);
        }
    }
}