package me.tiary.controller.commentcontroller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import common.annotation.controller.ControllerTest;
import common.config.url.CommentApiUrl;
import common.factory.dto.comment.CommentListReadResponseDtoFactory;
import common.utility.GsonLocalDateTimeDeserializer;
import common.utility.GsonLocalDateTimeSerializer;
import me.tiary.controller.CommentController;
import me.tiary.dto.comment.CommentListReadResponseDto;
import me.tiary.exception.CommentException;
import me.tiary.exception.handler.ExceptionResponse;
import me.tiary.exception.handler.controller.GlobalExceptionHandler;
import me.tiary.exception.status.CommentStatus;
import me.tiary.service.CommentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ControllerTest
@DisplayName("[CommentController] readCommentList")
class ReadCommentListTest {
    @InjectMocks
    private CommentController commentController;

    @Mock
    private CommentService commentService;

    private MockMvc mockMvc;

    private Gson gson;

    @BeforeEach
    void beforeEach() {
        mockMvc = MockMvcBuilders.standaloneSetup(commentController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        final GsonBuilder gsonBuilder = new GsonBuilder();

        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new GsonLocalDateTimeSerializer());
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new GsonLocalDateTimeDeserializer());

        gson = gsonBuilder.create();
    }

    @Test
    @DisplayName("[Fail] til does not exist")
    void failIfTilDoesNotExist() throws Exception {
        // Given
        final String tilUuid = UUID.randomUUID().toString();

        final String url = CommentApiUrl.COMMENT_LIST_READ.getEntireUrl() + tilUuid;

        final Pageable pageable = PageRequest.of(0, 5, Sort.by("createdDate").descending());

        doThrow(new CommentException(CommentStatus.NOT_EXISTING_TIL))
                .when(commentService)
                .readCommentList(tilUuid, pageable);

        // When
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get(url)
                        .param("page", "0")
                        .param("size", "5")
        );

        final ExceptionResponse response = gson.fromJson(
                resultActions.andReturn()
                        .getResponse()
                        .getContentAsString(StandardCharsets.UTF_8),
                ExceptionResponse.class
        );

        // Then
        resultActions.andExpect(status().is(CommentStatus.NOT_EXISTING_TIL.getHttpStatus().value()));
        assertThat(response.getMessages()).contains(CommentStatus.NOT_EXISTING_TIL.getMessage());
    }

    @Test
    @DisplayName("[Success] comments do not exist")
    void successIfCommentsDoNotExist() throws Exception {
        // Given
        final String tilUuid = UUID.randomUUID().toString();

        final String url = CommentApiUrl.COMMENT_LIST_READ.getEntireUrl() + tilUuid;

        final Pageable pageable = PageRequest.of(0, 5, Sort.by("createdDate").descending());

        final CommentListReadResponseDto responseDto = CommentListReadResponseDtoFactory.create(new ArrayList<>(), 0);

        doReturn(responseDto)
                .when(commentService)
                .readCommentList(tilUuid, pageable);

        // When
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get(url)
                        .param("page", "0")
                        .param("size", "5")
        );

        final CommentListReadResponseDto response = gson.fromJson(
                resultActions.andReturn()
                        .getResponse()
                        .getContentAsString(StandardCharsets.UTF_8),
                CommentListReadResponseDto.class
        );

        // Then
        resultActions.andExpect(status().isOk());
        assertThat(response.getComments()).isEmpty();
        assertThat(response.getTotalPages()).isEqualTo(responseDto.getTotalPages());
    }

    @Test
    @DisplayName("[Success] comments do exist")
    void successIfCommentsDoExist() throws Exception {
        // Given
        final String tilUuid = UUID.randomUUID().toString();

        final String url = CommentApiUrl.COMMENT_LIST_READ.getEntireUrl() + tilUuid;

        final Pageable pageable = PageRequest.of(0, 5, Sort.by("createdDate").descending());

        final CommentListReadResponseDto responseDto = CommentListReadResponseDtoFactory.createDefaultCommentListReadResponseDto();

        doReturn(responseDto)
                .when(commentService)
                .readCommentList(tilUuid, pageable);

        // When
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get(url)
                        .param("page", "0")
                        .param("size", "5")
        );

        final CommentListReadResponseDto response = gson.fromJson(
                resultActions.andReturn()
                        .getResponse()
                        .getContentAsString(StandardCharsets.UTF_8),
                CommentListReadResponseDto.class
        );

        // Then
        resultActions.andExpect(status().isOk());
        assertThat(response.getComments().get(0).getUuid()).hasSize(36);
        assertThat(response.getComments().get(0).getNickname()).isEqualTo(responseDto.getComments().get(0).getNickname());
        assertThat(response.getComments().get(0).getContent()).isEqualTo(responseDto.getComments().get(0).getContent());
        assertThat(response.getComments().get(0).getCreatedDate()).isEqualTo(responseDto.getComments().get(0).getCreatedDate().truncatedTo(ChronoUnit.SECONDS));
        assertThat(response.getTotalPages()).isEqualTo(responseDto.getTotalPages());
    }
}