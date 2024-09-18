package me.tiary.controller.viewcontroller;

import common.annotation.controller.ControllerIntegrationTest;
import common.config.factory.FactoryPreset;
import common.config.url.ViewUrl;
import me.tiary.controller.ViewController;
import me.tiary.properties.jwt.AccessTokenProperties;
import me.tiary.service.ProfileService;
import me.tiary.service.TilService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.servlet.http.Cookie;

import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ControllerIntegrationTest(ViewController.class)
@DisplayName("[ViewController - Integration] directIndexView")
class DirectIndexViewIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProfileService profileService;

    @MockBean
    private TilService tilService;

    @Test
    @DisplayName("[Success] anonymous views index")
    void successIfAnonymousViewsIndex() throws Exception {
        // Given
        final String url = ViewUrl.INDEX.getEntireUrl();

        // When
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get(url)
        );

        // Then
        resultActions.andExpect(status().isOk());
        resultActions.andExpect(content().contentType("text/html;charset=UTF-8"));
    }

    @Test
    @DisplayName("[Success] member views index")
    void successIfMemberViewsIndex() throws Exception {
        // Given
        final String url = ViewUrl.INDEX.getEntireUrl();

        // Algorithm = HMAC256, Payload = { "uuid": "cbf0f220-97b8-4312-82ce-f98266c428d4" }, Secret Key = jwt-access-token-secret-key
        final String accessToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1dWlkIjoiY2JmMGYyMjAtOTdiOC00MzEyLTgyY2UtZjk4MjY2YzQyOGQ0In0.rftGC07wvthl89A-lHN4NzeP2gcVv9UxTTnST3Nhqz8";

        doReturn(FactoryPreset.NICKNAME)
                .when(profileService)
                .searchNicknameUsingUuid("cbf0f220-97b8-4312-82ce-f98266c428d4");

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