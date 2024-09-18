package me.tiary.controller.tilcontroller;

import common.annotation.controller.ControllerIntegrationTest;
import common.config.url.TilApiUrl;
import me.tiary.controller.TilController;
import me.tiary.domain.Profile;
import me.tiary.service.TilService;
import me.tiary.utility.common.StringUtility;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ControllerIntegrationTest(TilController.class)
@DisplayName("[TilController - Integration] readTilStreak")
class ReadTilStreakIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TilService tilService;

    @Test
    @DisplayName("[Fail] nickname is blank")
    void failIfNicknameIsBlank() throws Exception {
        // Given
        final String url = TilApiUrl.TIL_STREAK_READ.getEntireUrl() + " ";

        // When
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get(url)
                        .param("startDate", LocalDate.now().toString())
                        .param("endDate", LocalDate.now().toString())
        );

        // Then
        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("[Fail] nickname exceeds max length")
    void failIfNicknameExceedsMaxLength() throws Exception {
        // Given
        final String nickname = StringUtility.generateRandomString(Profile.NICKNAME_MAX_LENGTH + 1);

        final String url = TilApiUrl.TIL_STREAK_READ.getEntireUrl() + nickname;

        // When
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get(url)
                        .param("startDate", LocalDate.now().toString())
                        .param("endDate", LocalDate.now().toString())
        );

        // Then
        resultActions.andExpect(status().isBadRequest());
    }
}