package me.tiary.service.commentservice;

import common.annotation.service.ServiceTest;
import common.factory.domain.CommentFactory;
import common.factory.domain.ProfileFactory;
import common.factory.domain.TilFactory;
import me.tiary.domain.Comment;
import me.tiary.domain.Profile;
import me.tiary.domain.Til;
import me.tiary.dto.comment.CommentDeletionResponseDto;
import me.tiary.exception.CommentException;
import me.tiary.exception.status.CommentStatus;
import me.tiary.repository.CommentRepository;
import me.tiary.service.CommentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.modelmapper.ModelMapper;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;

@ServiceTest
@DisplayName("[CommentService] deleteComment")
class DeleteCommentTest {
    @InjectMocks
    private CommentService commentService;

    @Mock
    private CommentRepository commentRepository;

    @Spy
    private ModelMapper modelMapper;

    private Profile profile;

    private Til til;

    @BeforeEach
    void beforeEach() {
        modelMapper.getConfiguration()
                .setFieldMatchingEnabled(true)
                .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE);

        profile = ProfileFactory.createDefaultProfile();

        til = TilFactory.createDefaultTil(profile);
    }

    @Test
    @DisplayName("[Fail] comment does not exist")
    void failIfCommentDoesNotExist() {
        // Given
        final String commentUuid = UUID.randomUUID().toString();

        doReturn(Optional.empty())
                .when(commentRepository)
                .findByUuidJoinFetchProfile(commentUuid);

        final String profileUuid = UUID.randomUUID().toString();

        // When, Then
        final CommentException result = assertThrows(CommentException.class, () -> commentService.deleteComment(profileUuid, commentUuid));

        assertThat(result.getStatus()).isEqualTo(CommentStatus.NOT_EXISTING_COMMENT);
    }

    @Test
    @DisplayName("[Fail] member does not have authorization")
    void failIfMemberDoesNotHaveAuthorization() {
        // Given
        final Comment comment = CommentFactory.createDefaultComment(profile, til);

        doReturn(Optional.of(comment))
                .when(commentRepository)
                .findByUuidJoinFetchProfile(comment.getUuid());

        final String profileUuid = UUID.randomUUID().toString();

        // When, Then
        final CommentException result = assertThrows(CommentException.class, () -> commentService.deleteComment(profileUuid, comment.getUuid()));

        assertThat(result.getStatus()).isEqualTo(CommentStatus.NOT_AUTHORIZED_MEMBER);
    }

    @Test
    @DisplayName("[Success] comment is deleted")
    void successIfCommentIsDeleted() {
        // Given
        final Comment comment = CommentFactory.createDefaultComment(profile, til);

        doReturn(Optional.of(comment))
                .when(commentRepository)
                .findByUuidJoinFetchProfile(comment.getUuid());

        // When
        final CommentDeletionResponseDto result = commentService.deleteComment(profile.getUuid(), comment.getUuid());

        // Then
        assertThat(result.getUuid()).hasSize(36);
    }
}