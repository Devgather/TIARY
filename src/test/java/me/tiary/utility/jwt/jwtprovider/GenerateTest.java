package me.tiary.utility.jwt.jwtprovider;

import com.auth0.jwt.interfaces.DecodedJWT;
import common.factory.utility.jwt.JwtProviderFactory;
import me.tiary.properties.jwt.JwtProperties;
import me.tiary.utility.jwt.JwtProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("[JwtProvider] generate")
class GenerateTest {
    private JwtProvider jwtProvider;

    @BeforeEach
    void beforeEach() {
        final JwtProperties properties = new TestTokenProperties("Test");

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
        final String token = jwtProvider.generate(payload);

        final DecodedJWT result = jwtProvider.verify(token);

        // Then
        assertThat(result.getClaim("key").asString()).isEqualTo(payload.get("key"));
    }

    static class TestTokenProperties extends JwtProperties {
        public TestTokenProperties(final String secretKey) {
            super(secretKey);
        }
    }
}