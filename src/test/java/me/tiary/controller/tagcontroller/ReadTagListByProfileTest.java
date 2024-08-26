package me.tiary.controller.tagcontroller;

import com.google.gson.Gson;
import common.annotation.controller.ControllerTest;
import common.config.factory.FactoryPreset;
import common.config.url.TagApiUrl;
import common.factory.dto.tag.TagListReadResponseDtoFactory;
import me.tiary.controller.TagController;
import me.tiary.dto.tag.TagListReadResponseDto;
import me.tiary.exception.TagException;
import me.tiary.exception.handler.ExceptionResponse;
import me.tiary.exception.handler.controller.GlobalExceptionHandler;
import me.tiary.exception.status.TagStatus;
import me.tiary.service.TagService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ControllerTest
@DisplayName("[TagController] readTagListByProfile")
class ReadTagListByProfileTest {
    @InjectMocks
    private TagController tagController;

    @Mock
    private TagService tagService;

    private MockMvc mockMvc;

    private Gson gson;

    @BeforeEach
    void beforeEach() {
        mockMvc = MockMvcBuilders.standaloneSetup(tagController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        gson = new Gson();
    }

    @Test
    @DisplayName("[Fail] profile does not exist")
    void failIfProfileDoesNotExist() throws Exception {
        // Given
        final String nickname = FactoryPreset.NICKNAME;

        final String url = TagApiUrl.TAG_LIST_READ_BY_PROFILE.getEntireUrl() + nickname;

        doThrow(new TagException(TagStatus.NOT_EXISTING_PROFILE))
                .when(tagService)
                .readTagListByProfile(nickname);

        // When
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get(url)
        );

        final ExceptionResponse response = gson.fromJson(
                resultActions.andReturn()
                        .getResponse()
                        .getContentAsString(StandardCharsets.UTF_8),
                ExceptionResponse.class
        );

        // Then
        resultActions.andExpect(status().is(TagStatus.NOT_EXISTING_PROFILE.getHttpStatus().value()));
        assertThat(response.getMessages()).contains(TagStatus.NOT_EXISTING_PROFILE.getMessage());
    }

    @Test
    @DisplayName("[Success] tags do not exist")
    void successIfTagsDoNotExist() throws Exception {
        // Given
        final String nickname = FactoryPreset.NICKNAME;

        final String url = TagApiUrl.TAG_LIST_READ_BY_PROFILE.getEntireUrl() + nickname;

        final TagListReadResponseDto responseDto = TagListReadResponseDtoFactory.create(new ArrayList<>());

        doReturn(responseDto)
                .when(tagService)
                .readTagListByProfile(nickname);

        // When
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get(url)
        );

        final TagListReadResponseDto response = gson.fromJson(
                resultActions.andReturn()
                        .getResponse()
                        .getContentAsString(StandardCharsets.UTF_8),
                TagListReadResponseDto.class
        );

        // Then
        resultActions.andExpect(status().isOk());
        assertThat(response.getTags()).isEmpty();
    }

    @Test
    @DisplayName("[Success] tags do exist")
    void successIfTagsDoExist() throws Exception {
        // Given
        final String nickname = FactoryPreset.NICKNAME;

        final String url = TagApiUrl.TAG_LIST_READ_BY_PROFILE.getEntireUrl() + nickname;

        final TagListReadResponseDto responseDto = TagListReadResponseDtoFactory.createDefaultTagListReadResponseDto();

        doReturn(responseDto)
                .when(tagService)
                .readTagListByProfile(nickname);

        // When
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get(url)
        );

        final TagListReadResponseDto response = gson.fromJson(
                resultActions.andReturn()
                        .getResponse()
                        .getContentAsString(StandardCharsets.UTF_8),
                TagListReadResponseDto.class
        );

        // Then
        resultActions.andExpect(status().isOk());
        assertThat(response.getTags()).hasSize(responseDto.getTags().size());
    }
}