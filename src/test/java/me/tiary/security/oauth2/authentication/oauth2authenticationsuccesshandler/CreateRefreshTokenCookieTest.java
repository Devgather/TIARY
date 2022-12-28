package me.tiary.security.oauth2.authentication.oauth2authenticationsuccesshandler;

import common.factory.utility.jwt.JwtProviderFactory;
import me.tiary.properties.jwt.RefreshTokenProperties;
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

@DisplayName("[OAuth2AuthenticationSuccessHandler] createRefreshTokenCookie")
class CreateRefreshTokenCookieTest {
    public static final String METHOD_NAME = "createRefreshTokenCookie";

    private OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;

    private Method createRefreshTokenCookieMethod;

    private JwtProvider refreshTokenProvider;

    @BeforeEach
    void beforeEach() throws Exception {
        refreshTokenProvider = JwtProviderFactory.createRefreshTokenProvider();

        oAuth2AuthenticationSuccessHandler = new OAuth2AuthenticationSuccessHandler(
                null, null, null, refreshTokenProvider, null
        );

        createRefreshTokenCookieMethod = oAuth2AuthenticationSuccessHandler.getClass().getDeclaredMethod(
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
                oAuth2AuthenticationSuccessHandler, payload
        ));

        assertThat(result.getCause().getClass()).isEqualTo(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("[Success] refresh token cookie is created")
    void successIfRefreshTokenCookieIsCreated() throws Exception {
        // Given
        final Map<String, String> payload = Map.of("key", "value");

        // When
        final Cookie result = (Cookie) createRefreshTokenCookieMethod.invoke(oAuth2AuthenticationSuccessHandler, payload);

        // Then
        assertThat(result.isHttpOnly()).isTrue();
        assertThat(result.getPath()).isEqualTo("/");
        assertThat(result.getMaxAge()).isEqualTo(refreshTokenProvider.getValidSeconds());
        assertThat(result.getName()).isEqualTo(RefreshTokenProperties.COOKIE_NAME);
        assertThat(result.getValue()).isNotNull();
    }
}