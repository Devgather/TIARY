package me.tiary.controller.tilcontroller;

import common.annotation.controller.ControllerIntegrationTest;
import common.config.factory.FactoryPreset;
import common.config.url.TilApiUrl;
import me.tiary.controller.TilController;
import me.tiary.domain.Profile;
import me.tiary.service.TilService;
import me.tiary.utility.common.StringUtility;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ControllerIntegrationTest(TilController.class)
@DisplayName("[TilController - Integration] readTilList")
class ReadTilListIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TilService tilService;

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {FactoryPreset.TAG})
    @DisplayName("[Fail] nickname is blank")
    void failIfNicknameIsBlank(final String tag) throws Exception {
        // Given
        final String url = TilApiUrl.TIL_LIST_READ.getEntireUrl() + " ";

        // When
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get(url)
                        .param("tag", tag)
                        .param("page", "0")
                        .param("size", "5")
        );

        // Then
        resultActions.andExpect(status().isBadRequest());
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {FactoryPreset.TAG})
    @DisplayName("[Fail] nickname exceeds max length")
    void failIfNicknameExceedsMaxLength(final String tag) throws Exception {
        // Given
        final String nickname = StringUtility.generateRandomString(Profile.NICKNAME_MAX_LENGTH + 1);

        final String url = TilApiUrl.TIL_LIST_READ.getEntireUrl() + nickname;

        // When
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get(url)
                        .param("tag", tag)
                        .param("page", "0")
                        .param("size", "5")
        );

        // Then
        resultActions.andExpect(status().isBadRequest());
    }
}