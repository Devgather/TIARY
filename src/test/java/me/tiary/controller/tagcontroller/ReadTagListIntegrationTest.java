package me.tiary.controller.tagcontroller;

import common.annotation.controller.ControllerIntegrationTest;
import common.config.url.TagApiUrl;
import me.tiary.controller.TagController;
import me.tiary.service.TagService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ControllerIntegrationTest(TagController.class)
@DisplayName("[TagController - Integration] readTagList")
class ReadTagListIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TagService tagService;

    @Test
    @DisplayName("[Fail] til uuid is blank")
    void failIfTilUuidIsBlank() throws Exception {
        // Given
        final String url = TagApiUrl.TAG_LIST_READ.getEntireUrl() + " ";

        // When
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get(url)
        );

        // Then
        resultActions.andExpect(status().isBadRequest());
    }
}