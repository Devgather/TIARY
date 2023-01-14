package me.tiary.controller.commentcontroller;

import com.google.gson.Gson;
import common.annotation.controller.ControllerTest;
import common.config.factory.FactoryPreset;
import common.config.url.CommentApiUrl;
import common.factory.dto.comment.CommentWritingRequestDtoFactory;
import common.factory.dto.comment.CommentWritingResponseDtoFactory;
import common.resolver.argument.AuthenticationPrincipalArgumentResolver;
import me.tiary.controller.CommentController;
import me.tiary.dto.comment.CommentWritingRequestDto;
import me.tiary.dto.comment.CommentWritingResponseDto;
import me.tiary.exception.CommentException;
import me.tiary.exception.handler.ExceptionResponse;
import me.tiary.exception.handler.controller.GlobalExceptionHandler;
import me.tiary.exception.status.CommentStatus;
import me.tiary.security.web.userdetails.MemberDetails;
import me.tiary.service.CommentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ControllerTest
@DisplayName("[CommentController] writeComment")
class WriteCommentTest {
    @InjectMocks
    private CommentController commentController;

    @Mock
    private CommentService commentService;

    private MemberDetails memberDetails;

    private MockMvc mockMvc;

    private Gson gson;

    @BeforeEach
    void beforeEach() {
        memberDetails = MemberDetails.builder()
                .profileUuid(UUID.randomUUID().toString())
                .build();

        mockMvc = MockMvcBuilders
                .standaloneSetup(commentController)
                .setCustomArgumentResolvers(new AuthenticationPrincipalArgumentResolver(memberDetails))
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        gson = new Gson();
    }

    @Test
    @DisplayName("[Fail] til uuid is null")
    void failIfTilUuidIsNull() throws Exception {
        // Given
        final String url = CommentApiUrl.COMMENT_WRITING.getEntireUrl();

        final CommentWritingRequestDto requestDto = CommentWritingRequestDtoFactory.create(null, FactoryPreset.CONTENT);

        // When
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post(url)
                        .content(gson.toJson(requestDto))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // Then
        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("[Fail] til uuid is empty")
    void failIfTilUuidIsEmpty() throws Exception {
        // Given
        final String url = CommentApiUrl.COMMENT_WRITING.getEntireUrl();

        final CommentWritingRequestDto requestDto = CommentWritingRequestDtoFactory.create("", FactoryPreset.CONTENT);

        // When
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post(url)
                        .content(gson.toJson(requestDto))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // Then
        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("[Fail] til uuid is blank")
    void failIfTilUuidIsBlank() throws Exception {
        // Given
        final String url = CommentApiUrl.COMMENT_WRITING.getEntireUrl();

        final CommentWritingRequestDto requestDto = CommentWritingRequestDtoFactory.create(" ", FactoryPreset.CONTENT);

        // When
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post(url)
                        .content(gson.toJson(requestDto))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // Then
        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("[Fail] content is null")
    void failIfContentIsNull() throws Exception {
        // Given
        final String url = CommentApiUrl.COMMENT_WRITING.getEntireUrl();

        final String tilUuid = UUID.randomUUID().toString();

        final CommentWritingRequestDto requestDto = CommentWritingRequestDtoFactory.create(tilUuid, null);

        // When
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post(url)
                        .content(gson.toJson(requestDto))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // Then
        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("[Fail] content is empty")
    void failIfContentIsEmpty() throws Exception {
        // Given
        final String url = CommentApiUrl.COMMENT_WRITING.getEntireUrl();

        final String tilUuid = UUID.randomUUID().toString();

        final CommentWritingRequestDto requestDto = CommentWritingRequestDtoFactory.create(tilUuid, "");

        // When
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post(url)
                        .content(gson.toJson(requestDto))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // Then
        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("[Fail] content is blank")
    void failIfContentIsBlank() throws Exception {
        // Given
        final String url = CommentApiUrl.COMMENT_WRITING.getEntireUrl();

        final String tilUuid = UUID.randomUUID().toString();

        final CommentWritingRequestDto requestDto = CommentWritingRequestDtoFactory.create(tilUuid, " ");

        // When
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post(url)
                        .content(gson.toJson(requestDto))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // Then
        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("[Fail] profile does not exist")
    void failIfProfileDoesNotExist() throws Exception {
        // Given
        final String url = CommentApiUrl.COMMENT_WRITING.getEntireUrl();

        final String tilUuid = UUID.randomUUID().toString();

        final CommentWritingRequestDto requestDto = CommentWritingRequestDtoFactory.createDefaultCommentWritingRequestDto(tilUuid);

        doThrow(new CommentException(CommentStatus.NOT_EXISTING_PROFILE))
                .when(commentService)
                .writeComment(memberDetails.getProfileUuid(), requestDto);

        // When
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post(url)
                        .content(gson.toJson(requestDto))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        final ExceptionResponse response = gson.fromJson(
                resultActions.andReturn()
                        .getResponse()
                        .getContentAsString(StandardCharsets.UTF_8), ExceptionResponse.class
        );

        // Then
        resultActions.andExpect(status().is(CommentStatus.NOT_EXISTING_PROFILE.getHttpStatus().value()));
        assertThat(response.getMessages()).contains(CommentStatus.NOT_EXISTING_PROFILE.getMessage());
    }

    @Test
    @DisplayName("[Fail] til does not exist")
    void failIfTilDoesNotExist() throws Exception {
        // Given
        final String url = CommentApiUrl.COMMENT_WRITING.getEntireUrl();

        final String tilUuid = UUID.randomUUID().toString();

        final CommentWritingRequestDto requestDto = CommentWritingRequestDtoFactory.createDefaultCommentWritingRequestDto(tilUuid);

        doThrow(new CommentException(CommentStatus.NOT_EXISTING_TIL))
                .when(commentService)
                .writeComment(memberDetails.getProfileUuid(), requestDto);

        // When
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post(url)
                        .content(gson.toJson(requestDto))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        final ExceptionResponse response = gson.fromJson(
                resultActions.andReturn()
                        .getResponse()
                        .getContentAsString(StandardCharsets.UTF_8), ExceptionResponse.class
        );

        // Then
        resultActions.andExpect(status().is(CommentStatus.NOT_EXISTING_TIL.getHttpStatus().value()));
        assertThat(response.getMessages()).contains(CommentStatus.NOT_EXISTING_TIL.getMessage());
    }

    @Test
    @DisplayName("[Success] comment is acceptable")
    void successIfCommentIsAcceptable() throws Exception {
        // Given
        final String url = CommentApiUrl.COMMENT_WRITING.getEntireUrl();

        final String tilUuid = UUID.randomUUID().toString();

        final CommentWritingRequestDto requestDto = CommentWritingRequestDtoFactory.createDefaultCommentWritingRequestDto(tilUuid);

        final CommentWritingResponseDto responseDto = CommentWritingResponseDtoFactory.createDefaultCommentWritingResponseDto();

        doReturn(responseDto)
                .when(commentService)
                .writeComment(memberDetails.getProfileUuid(), requestDto);

        // When
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post(url)
                        .content(gson.toJson(requestDto))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        final CommentWritingResponseDto response = gson.fromJson(resultActions
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8), CommentWritingResponseDto.class);

        // Then
        resultActions.andExpect(status().isCreated());
        assertThat(response.getUuid().length()).isEqualTo(36);
    }
}