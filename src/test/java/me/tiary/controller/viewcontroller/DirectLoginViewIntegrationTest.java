package me.tiary.controller.viewcontroller;

import common.annotation.controller.ControllerIntegrationTest;
import common.config.url.ViewUrl;
import me.tiary.controller.ViewController;
import me.tiary.repository.OAuthRepository;
import me.tiary.repository.ProfileRepository;
import me.tiary.service.ProfileService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ControllerIntegrationTest(ViewController.class)
@DisplayName("[ViewController - Integration] directLoginView")
class DirectLoginViewIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProfileService profileService;

    @MockBean
    private ProfileRepository profileRepository;

    @MockBean
    private OAuthRepository oAuthRepository;

    @Test
    @DisplayName("[Success] login view is rendered")
    void successIfLoginViewIsRendered() throws Exception {
        // Given
        final String url = ViewUrl.LOGIN.getEntireUrl();

        // When
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get(url)
        );

        // Then
        resultActions.andExpect(status().isOk());
        resultActions.andExpect(content().contentType("text/html;charset=UTF-8"));
    }
}