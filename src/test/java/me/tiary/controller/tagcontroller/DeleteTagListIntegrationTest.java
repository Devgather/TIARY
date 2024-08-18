package me.tiary.controller.tagcontroller;

import common.annotation.controller.ControllerIntegrationTest;
import common.config.url.TagApiUrl;
import me.tiary.controller.TagController;
import me.tiary.properties.jwt.AccessTokenProperties;
import me.tiary.service.TagService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.servlet.http.Cookie;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ControllerIntegrationTest(TagController.class)
@DisplayName("[TagController - Integration] deleteTagList")
class DeleteTagListIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TagService tagService;

    @Test
    @DisplayName("[Fail] til uuid is blank")
    void failIfTilUuidIsBlank() throws Exception {
        // Given
        final String url = TagApiUrl.TAG_LIST_DELETION.getEntireUrl() + " ";

        // Algorithm = HMAC256, Payload = { "uuid": "cbf0f220-97b8-4312-82ce-f98266c428d4" }, Secret Key = jwt-access-token-secret-key
        final String accessToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1dWlkIjoiY2JmMGYyMjAtOTdiOC00MzEyLTgyY2UtZjk4MjY2YzQyOGQ0In0.rftGC07wvthl89A-lHN4NzeP2gcVv9UxTTnST3Nhqz8";

        // When
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.delete(url)
                        .with(csrf())
                        .cookie(new Cookie(AccessTokenProperties.COOKIE_NAME, accessToken))
        );

        // Then
        resultActions.andExpect(status().isBadRequest());
    }
}