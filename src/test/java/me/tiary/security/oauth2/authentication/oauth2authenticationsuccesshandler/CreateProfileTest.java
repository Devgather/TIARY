package me.tiary.security.oauth2.authentication.oauth2authenticationsuccesshandler;

import common.factory.domain.ProfileFactory;
import me.tiary.domain.Profile;
import me.tiary.repository.ProfileRepository;
import me.tiary.security.oauth2.authentication.OAuth2AuthenticationSuccessHandler;
import me.tiary.utility.common.StringUtility;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Method;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
@DisplayName("[OAuth2AuthenticationSuccessHandler] createProfile")
class CreateProfileTest {
    public static final String METHOD_NAME = "createProfile";

    @InjectMocks
    private OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;

    private Method createProfileMethod;

    @Mock
    private ProfileRepository profileRepository;

    @BeforeEach
    void beforeEach() throws Exception {
        createProfileMethod = oAuth2AuthenticationSuccessHandler.getClass().getDeclaredMethod(METHOD_NAME);

        createProfileMethod.setAccessible(true);
    }

    @Test
    @DisplayName("[Success] profile is created")
    void successIfProfileIsCreated() throws Exception {
        // Given
        final Profile profile = ProfileFactory.create(
                StringUtility.generateRandomString(Profile.NICKNAME_MAX_LENGTH), Profile.BASIC_PICTURE
        );

        doReturn(Optional.empty())
                .when(profileRepository)
                .findByNickname(any(String.class));

        doReturn(profile)
                .when(profileRepository)
                .save(any(Profile.class));

        // When
        final Profile result = (Profile) createProfileMethod.invoke(oAuth2AuthenticationSuccessHandler);

        // Then
        assertThat(result).isNotNull();
    }
}