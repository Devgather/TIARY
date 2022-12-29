package me.tiary.security.oauth2.authentication.oauth2authenticationsuccesshandler;

import common.factory.utility.jwt.JwtProviderFactory;
import me.tiary.properties.jwt.AccessTokenProperties;
import me.tiary.security.oauth2.authentication.OAuth2AuthenticationSuccessHandler;
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

@DisplayName("[OAuth2AuthenticationSuccessHandler] createAccessTokenCookie")
class CreateAccessTokenCookieTest {
    public static final String METHOD_NAME = "createAccessTokenCookie";

    private OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;

    private Method createAccessTokenCookieMethod;

    @BeforeEach
    void beforeEach() throws Exception {
        final JwtProvider accessTokenProvider = JwtProviderFactory.createAccessTokenProvider();

        oAuth2AuthenticationSuccessHandler = new OAuth2AuthenticationSuccessHandler(
                null, null, accessTokenProvider, null, null
        );

        createAccessTokenCookieMethod = oAuth2AuthenticationSuccessHandler.getClass().getDeclaredMethod(
                METHOD_NAME, Map.class
        );

        createAccessTokenCookieMethod.setAccessible(true);
    }

    @Test
    @DisplayName("[Fail] payload type is invalid")
    void failIfPayloadTypeIsInvalid() {
        // Given
        final Map<String, Short> payload = Map.of("key", (short) 0);

        // When, Then
        final InvocationTargetException result = assertThrows(InvocationTargetException.class, () -> createAccessTokenCookieMethod.invoke(
                oAuth2AuthenticationSuccessHandler, payload
        ));

        assertThat(result.getCause().getClass()).isEqualTo(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("[Success] access token cookie is created")
    void successIfAccessTokenCookieIsCreated() throws Exception {
        // Given
        final Map<String, String> payload = Map.of("key", "value");

        // When
        final Cookie result = (Cookie) createAccessTokenCookieMethod.invoke(oAuth2AuthenticationSuccessHandler, payload);

        // Then
        assertThat(result.isHttpOnly()).isTrue();
        assertThat(result.getSecure()).isTrue();
        assertThat(result.getPath()).isEqualTo("/");
        assertThat(result.getName()).isEqualTo(AccessTokenProperties.COOKIE_NAME);
        assertThat(result.getValue()).isNotNull();
    }
}