package me.tiary.controller.commentcontroller;

import common.annotation.controller.ControllerIntegrationTest;
import common.config.url.CommentApiUrl;
import me.tiary.controller.CommentController;
import me.tiary.service.CommentService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ControllerIntegrationTest(CommentController.class)
@DisplayName("[CommentController - Integration] readCommentList")
class ReadCommentListIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CommentService commentService;

    @Test
    @DisplayName("[Fail] til uuid is blank")
    void failIfTilUuidIsBlank() throws Exception {
        // Given
        final String url = CommentApiUrl.COMMENT_LIST_READ.getEntireUrl() + " ";

        // When
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get(url)
                        .param("page", "0")
                        .param("size", "5")
        );

        // Then
        resultActions.andExpect(status().isBadRequest());
    }
}