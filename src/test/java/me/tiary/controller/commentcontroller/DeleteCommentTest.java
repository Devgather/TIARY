package me.tiary.controller.commentcontroller;

import com.google.gson.Gson;
import common.annotation.controller.ControllerTest;
import common.config.url.CommentApiUrl;
import common.factory.dto.comment.CommentDeletionResponseDtoFactory;
import common.resolver.argument.AuthenticationPrincipalArgumentResolver;
import me.tiary.controller.CommentController;
import me.tiary.dto.comment.CommentDeletionResponseDto;
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
@DisplayName("[CommentController] deleteComment")
class DeleteCommentTest {
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
    @DisplayName("[Fail] comment does not exist")
    void failIfCommentDoesNotExist() throws Exception {
        // Given
        final String commentUuid = UUID.randomUUID().toString();

        final String url = CommentApiUrl.COMMENT_DELETION.getEntireUrl() + commentUuid;

        doThrow(new CommentException(CommentStatus.NOT_EXISTING_COMMENT))
                .when(commentService)
                .deleteComment(memberDetails.getProfileUuid(), commentUuid);

        // When
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.delete(url)
        );

        final ExceptionResponse response = gson.fromJson(
                resultActions.andReturn()
                        .getResponse()
                        .getContentAsString(StandardCharsets.UTF_8),
                ExceptionResponse.class
        );


        // Then
        resultActions.andExpect(status().is(CommentStatus.NOT_EXISTING_COMMENT.getHttpStatus().value()));
        assertThat(response.getMessages()).contains(CommentStatus.NOT_EXISTING_COMMENT.getMessage());
    }

    @Test
    @DisplayName("[Fail] member does not have authorization")
    void failIfMemberDoesNotHaveAuthorization() throws Exception {
        // Given
        final String commentUuid = UUID.randomUUID().toString();

        final String url = CommentApiUrl.COMMENT_DELETION.getEntireUrl() + commentUuid;

        doThrow(new CommentException(CommentStatus.NOT_AUTHORIZED_MEMBER))
                .when(commentService)
                .deleteComment(memberDetails.getProfileUuid(), commentUuid);

        // When
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.delete(url)
        );

        final ExceptionResponse response = gson.fromJson(
                resultActions.andReturn()
                        .getResponse()
                        .getContentAsString(StandardCharsets.UTF_8),
                ExceptionResponse.class
        );

        // Then
        resultActions.andExpect(status().is(CommentStatus.NOT_AUTHORIZED_MEMBER.getHttpStatus().value()));
        assertThat(response.getMessages()).contains(CommentStatus.NOT_AUTHORIZED_MEMBER.getMessage());
    }

    @Test
    @DisplayName("[Success] comment is deleted")
    void successIfCommentIsDeleted() throws Exception {
        // Given
        final String commentUuid = UUID.randomUUID().toString();

        final String url = CommentApiUrl.COMMENT_DELETION.getEntireUrl() + commentUuid;

        final CommentDeletionResponseDto responseDto = CommentDeletionResponseDtoFactory.createDefaultCommentDeletionResponseDto();

        doReturn(responseDto)
                .when(commentService)
                .deleteComment(memberDetails.getProfileUuid(), commentUuid);

        // When
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.delete(url)
        );

        final CommentDeletionResponseDto response = gson.fromJson(
                resultActions.andReturn()
                        .getResponse()
                        .getContentAsString(StandardCharsets.UTF_8),
                CommentDeletionResponseDto.class
        );

        // Then
        assertThat(response.getUuid()).hasSize(36);
    }
}