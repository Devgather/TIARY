package me.tiary.controller.viewcontroller;

import common.annotation.controller.ControllerIntegrationTest;
import common.config.url.ViewUrl;
import me.tiary.controller.ViewController;
import me.tiary.properties.jwt.AccessTokenProperties;
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

import javax.servlet.http.Cookie;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ControllerIntegrationTest(ViewController.class)
@DisplayName("[ViewController - Integration] directTilEditorView")
class DirectTilEditorViewIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProfileService profileService;

    @MockBean
    private ProfileRepository profileRepository;

    @MockBean
    private OAuthRepository oAuthRepository;

    @Test
    @DisplayName("[Success] til editor view is rendered")
    void successIfTilEditorViewIsRendered() throws Exception {
        // Given
        final String url = ViewUrl.TIL_EDITOR.getEntireUrl();

        // Algorithm = HMAC256, Payload = { "uuid": "cbf0f220-97b8-4312-82ce-f98266c428d4" }, Secret Key = jwt-access-token-secret-key
        final String accessToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1dWlkIjoiY2JmMGYyMjAtOTdiOC00MzEyLTgyY2UtZjk4MjY2YzQyOGQ0In0.rftGC07wvthl89A-lHN4NzeP2gcVv9UxTTnST3Nhqz8";

        // When
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get(url)
                        .cookie(new Cookie(AccessTokenProperties.COOKIE_NAME, accessToken))
        );

        // Then
        resultActions.andExpect(status().isOk());
        resultActions.andExpect(content().contentType("text/html;charset=UTF-8"));
    }
}