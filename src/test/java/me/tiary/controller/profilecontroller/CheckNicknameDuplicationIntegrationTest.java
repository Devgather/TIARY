package me.tiary.controller.profilecontroller;

import me.tiary.controller.ProfileController;
import me.tiary.domain.Profile;
import me.tiary.service.ProfileService;
import utility.StringUtility;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = ProfileController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
@MockBean(JpaMetamodelMappingContext.class)
@DisplayName("[ProfileController - Integration] checkNicknameDuplication")
class CheckNicknameDuplicationIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProfileService profileService;

    @Test
    @DisplayName("[Fail] nickname is blank")
    void failIfNicknameIsBlank() throws Exception {
        // Given
        final String url = "/api/profile/nickname/ ";

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
        final String url = "/api/profile/nickname/" + nickname;

        // When
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.head(url)
        );

        // Then
        resultActions.andExpect(status().isBadRequest());
    }
}
