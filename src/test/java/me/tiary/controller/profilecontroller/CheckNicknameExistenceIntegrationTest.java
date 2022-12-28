package me.tiary.controller.profilecontroller;

import common.annotation.controller.ControllerIntegrationTest;
import common.config.url.ProfileApiUrl;
import me.tiary.controller.ProfileController;
import me.tiary.domain.Profile;
import me.tiary.repository.OAuthRepository;
import me.tiary.repository.ProfileRepository;
import me.tiary.service.ProfileService;
import me.tiary.utility.common.StringUtility;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ControllerIntegrationTest(ProfileController.class)
@DisplayName("[ProfileController - Integration] checkNicknameExistence")
class CheckNicknameExistenceIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProfileService profileService;

    @MockBean
    private ProfileRepository profileRepository;

    @MockBean
    private OAuthRepository oAuthRepository;

    @Test
    @DisplayName("[Fail] nickname is blank")
    void failIfNicknameIsBlank() throws Exception {
        // Given
        final String url = ProfileApiUrl.NICKNAME_EXISTENCE_CHECK.getEntireUrl() + " ";

        // When
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.head(url)
        );

        // Then
        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("[Fail] nickname exceeds max length")
    void failIfNicknameExceedsMaxLength() throws Exception {
        // Given
        final String nickname = StringUtility.generateRandomString(Profile.NICKNAME_MAX_LENGTH + 1);
        final String url = ProfileApiUrl.NICKNAME_EXISTENCE_CHECK.getEntireUrl() + nickname;

        // When
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.head(url)
        );

        // Then
        resultActions.andExpect(status().isBadRequest());
    }
}
