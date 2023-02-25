package me.tiary.controller.commentcontroller;

import common.annotation.controller.ControllerIntegrationTest;
import common.config.url.CommentApiUrl;
import me.tiary.controller.CommentController;
import me.tiary.properties.jwt.AccessTokenProperties;
import me.tiary.service.CommentService;
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

@ControllerIntegrationTest(CommentController.class)
@DisplayName("[CommentController - Integration] deleteComment")
class DeleteCommentIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CommentService commentService;

    @Test
    @DisplayName("[Fail] uuid is blank")
    void failIfUuidIsBlank() throws Exception {
        // Given
        final String url = CommentApiUrl.COMMENT_DELETION.getEntireUrl() + " ";

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