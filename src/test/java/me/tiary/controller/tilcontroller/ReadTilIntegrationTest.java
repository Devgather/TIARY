package me.tiary.controller.tilcontroller;

import common.annotation.controller.ControllerIntegrationTest;
import common.config.url.TilApiUrl;
import me.tiary.controller.TilController;
import me.tiary.repository.OAuthRepository;
import me.tiary.repository.ProfileRepository;
import me.tiary.service.TilService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ControllerIntegrationTest(TilController.class)
@DisplayName("[TilController - Integration] readTil")
class ReadTilIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TilService tilService;

    @MockBean
    private ProfileRepository profileRepository;

    @MockBean
    private OAuthRepository oAuthRepository;

    @Test
    @DisplayName("[Fail] uuid is blank")
    void failIfUuidIsBlank() throws Exception {
        // Given
        final String url = TilApiUrl.TIL_READ.getEntireUrl() + " ";

        // When
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get(url)
        );

        // Then
        resultActions.andExpect(status().isBadRequest());
    }
}