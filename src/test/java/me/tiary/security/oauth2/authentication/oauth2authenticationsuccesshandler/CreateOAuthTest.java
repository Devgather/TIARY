package me.tiary.security.oauth2.authentication.oauth2authenticationsuccesshandler;

import common.config.factory.FactoryPreset;
import common.factory.domain.OAuthFactory;
import common.factory.domain.ProfileFactory;
import me.tiary.domain.OAuth;
import me.tiary.domain.Profile;
import me.tiary.repository.OAuthRepository;
import me.tiary.security.oauth2.authentication.OAuth2AuthenticationSuccessHandler;
import me.tiary.utility.common.StringUtility;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

@ExtendWith(MockitoExtension.class)
@DisplayName("[OAuth2AuthenticationSuccessHandler] createOAuth")
class CreateOAuthTest {
    public static final String METHOD_NAME = "createOAuth";

    @InjectMocks
    private OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;

    private Method createOAuthMethod;

    @Mock
    private OAuthRepository oAuthRepository;

    @BeforeEach
    void beforeEach() throws Exception {
        createOAuthMethod = oAuth2AuthenticationSuccessHandler.getClass().getDeclaredMethod(
                METHOD_NAME, Profile.class, String.class, String.class
        );

        createOAuthMethod.setAccessible(true);
    }

    @Test
    @DisplayName("[Fail] data violate integrity")
    void failIfDataViolateIntegrity() {
        // Given
        final String exceptionMessage = "could not execute statement; SQL [n/a]; constraint [null]; nested exception is org.hibernate.exception.ConstraintViolationException: could not execute statement";

        doThrow(new DataIntegrityViolationException(exceptionMessage))
                .when(oAuthRepository)
                .save(any(OAuth.class));

        // When, Then
        final InvocationTargetException result = assertThrows(InvocationTargetException.class, () -> createOAuthMethod.invoke(
                oAuth2AuthenticationSuccessHandler,
                null,
                StringUtility.generateRandomString(255),
                FactoryPreset.OAUTH_PROVIDER
        ));

        assertThat(result.getCause().getClass()).isEqualTo(IllegalArgumentException.class);
        assertThat(result.getCause().getMessage()).isEqualTo(exceptionMessage);
    }

    @Test
    @DisplayName("[Success] oauth is created")
    void successIfOAuthIsCreated() throws Exception {
        // Given
        final Profile profile = ProfileFactory.createDefaultProfile();

        final OAuth oAuth = OAuthFactory.createDefaultOAuth(profile);

        doReturn(oAuth)
                .when(oAuthRepository)
                .save(any(OAuth.class));

        // When
        final OAuth result = (OAuth) createOAuthMethod.invoke(
                oAuth2AuthenticationSuccessHandler, profile, StringUtility.generateRandomString(255), FactoryPreset.OAUTH_PROVIDER
        );

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getProfile()).isEqualTo(oAuth.getProfile());
        assertThat(result.getIdentifier()).isEqualTo(oAuth.getIdentifier());
        assertThat(result.getProvider()).isEqualTo(oAuth.getProvider());
    }
}