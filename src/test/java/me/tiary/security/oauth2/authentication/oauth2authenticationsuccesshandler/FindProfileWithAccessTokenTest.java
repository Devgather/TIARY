package me.tiary.security.oauth2.authentication.oauth2authenticationsuccesshandler;

import common.factory.domain.ProfileFactory;
import common.factory.utility.jwt.JwtProviderFactory;
import me.tiary.domain.Profile;
import me.tiary.repository.ProfileRepository;
import me.tiary.security.oauth2.authentication.OAuth2AuthenticationSuccessHandler;
import me.tiary.utility.jwt.JwtProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
@DisplayName("[OAuth2AuthenticationSuccessHandler] findProfileWithAccessToken")
class FindProfileWithAccessTokenTest {
    public static final String METHOD_NAME = "findProfileWithAccessToken";

    private OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;

    private Method findProfileWithAccessTokenMethod;

    @Mock
    private ProfileRepository profileRepository;

    @BeforeEach
    void beforeEach() throws Exception {
        final JwtProvider accessTokenProvider = JwtProviderFactory.createAccessTokenProvider();

        oAuth2AuthenticationSuccessHandler = new OAuth2AuthenticationSuccessHandler(
                null, profileRepository, accessTokenProvider, null, null
        );

        findProfileWithAccessTokenMethod = oAuth2AuthenticationSuccessHandler.getClass().getDeclaredMethod(
                METHOD_NAME, String.class
        );

        findProfileWithAccessTokenMethod.setAccessible(true);
    }

    @Test
    @DisplayName("[Fail] access token is null")
    void failIfAccessTokenIsNull() throws Exception {
        // Given
        final String accessToken = null;

        // When
        @SuppressWarnings("unchecked") final Optional<Profile> result = (Optional<Profile>) findProfileWithAccessTokenMethod.invoke(
                oAuth2AuthenticationSuccessHandler, accessToken
        );

        // Then
        assertThat(result).isEmpty();
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "a.b.c",
            // Algorithm = HMAC512, Payload = { "uuid": "cbf0f220-97b8-4312-82ce-f98266c428d4" }, Secret Key = jwt-access-token-secret-key
            "eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJ1dWlkIjoiY2JmMGYyMjAtOTdiOC00MzEyLTgyY2UtZjk4MjY2YzQyOGQ0In0.cCJL6etYw6r7tlXpuJqEQ7LccTOSsKbBW3LzavvqSvLPSJRBt8w7yMAB6d53Bs_FXf7YRqF5F9xrWyKMr7_KZw",
            // Algorithm = HMAC256, Payload = { "uuid": "cbf0f220-97b8-4312-82ce-f98266c428d4", "exp": 0 }, Secret Key = jwt-access-token-secret-key
            "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1dWlkIjoiY2JmMGYyMjAtOTdiOC00MzEyLTgyY2UtZjk4MjY2YzQyOGQ0IiwiZXhwIjowfQ.3xL9qBUNtPop2bKoKloECcG0Pu-nCJC7tdE3tTXJ2fk",
            // Algorithm = HMAC256, Payload = { "uuid": "cbf0f220-97b8-4312-82ce-f98266c428d4" }, Secret Key = invalid-secret-key
            "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1dWlkIjoiY2JmMGYyMjAtOTdiOC00MzEyLTgyY2UtZjk4MjY2YzQyOGQ0In0.ftqXO7VbB5rpAaJks-B9V2a43TmE23TOtTbVzzxAwg4"
    })
    @DisplayName("[Fail] access token is invalid")
    void failIfAccessTokenIsInvalid(final String accessToken) {
        // When, Then
        final InvocationTargetException result = assertThrows(InvocationTargetException.class, () -> findProfileWithAccessTokenMethod.invoke(
                oAuth2AuthenticationSuccessHandler, accessToken
        ));

        assertThat(result.getCause().getClass()).isEqualTo(BadCredentialsException.class);
    }

    @Test
    @DisplayName("[Fail] access token is valid and profile uuid does not exist")
    void failIfAccessTokenIsValidAndProfileUuidDoesNotExist() throws Exception {
        // Given
        // Algorithm = HMAC256, Payload = { "uuid": "cbf0f220-97b8-4312-82ce-f98266c428d4" }, Secret Key = jwt-access-token-secret-key
        final String accessToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1dWlkIjoiY2JmMGYyMjAtOTdiOC00MzEyLTgyY2UtZjk4MjY2YzQyOGQ0In0.rftGC07wvthl89A-lHN4NzeP2gcVv9UxTTnST3Nhqz8";

        doReturn(Optional.empty())
                .when(profileRepository)
                .findByUuid(any(String.class));

        // When
        @SuppressWarnings("unchecked") final Optional<Profile> result = (Optional<Profile>) findProfileWithAccessTokenMethod.invoke(
                oAuth2AuthenticationSuccessHandler, accessToken
        );

        // Then
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("[Success] access token is valid and profile uuid does exist")
    void successIfAccessTokenIsValidAndProfileUuidDoesExist() throws Exception {
        // Given
        // Algorithm = HMAC256, Payload = { "uuid": "cbf0f220-97b8-4312-82ce-f98266c428d4" }, Secret Key = jwt-access-token-secret-key
        final String accessToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1dWlkIjoiY2JmMGYyMjAtOTdiOC00MzEyLTgyY2UtZjk4MjY2YzQyOGQ0In0.rftGC07wvthl89A-lHN4NzeP2gcVv9UxTTnST3Nhqz8";

        final Profile profile = ProfileFactory.createDefaultProfile();

        doReturn(Optional.of(profile))
                .when(profileRepository)
                .findByUuid("cbf0f220-97b8-4312-82ce-f98266c428d4");

        // When
        @SuppressWarnings("unchecked") final Optional<Profile> result = (Optional<Profile>) findProfileWithAccessTokenMethod.invoke(
                oAuth2AuthenticationSuccessHandler, accessToken
        );

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().getNickname()).isEqualTo(profile.getNickname());
        assertThat(result.get().getPicture()).isEqualTo(profile.getPicture());
    }
}