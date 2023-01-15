package me.tiary.service.commentservice;

import common.annotation.service.ServiceTest;
import common.factory.domain.CommentFactory;
import common.factory.domain.ProfileFactory;
import common.factory.domain.TilFactory;
import me.tiary.domain.Comment;
import me.tiary.domain.Profile;
import me.tiary.domain.Til;
import me.tiary.dto.comment.CommentListReadResponseDto;
import me.tiary.exception.CommentException;
import me.tiary.exception.status.CommentStatus;
import me.tiary.repository.CommentRepository;
import me.tiary.repository.TilRepository;
import me.tiary.service.CommentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;

@ServiceTest
@DisplayName("[CommentService] readCommentList")
class ReadCommentListTest {
    @InjectMocks
    private CommentService commentService;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private TilRepository tilRepository;

    private Profile profile;

    private Til til;

    @BeforeEach
    void beforeEach() {
        profile = ProfileFactory.createDefaultProfile();

        til = TilFactory.createDefaultTil(profile);
    }

    @Test
    @DisplayName("[Fail] til does not exist")
    void failIfTilDoesNotExist() {
        // Given
        doReturn(Optional.empty())
                .when(tilRepository)
                .findByUuid(til.getUuid());

        final Pageable pageable = PageRequest.of(0, 5, Sort.by("createdDate").descending());

        // When, Then
        final CommentException result = assertThrows(CommentException.class, () -> commentService.readCommentList(til.getUuid(), pageable));

        assertThat(result.getStatus()).isEqualTo(CommentStatus.NOT_EXISTING_TIL);
    }

    @Test
    @DisplayName("[Success] comments do not exist")
    void successIfCommentsDoNotExist() {
        // Given
        doReturn(Optional.of(til))
                .when(tilRepository)
                .findByUuid(til.getUuid());

        final Pageable pageable = PageRequest.of(0, 5, Sort.by("createdDate").descending());

        final Page<Til> commentPage = new PageImpl<>(new ArrayList<>(), pageable, 0);

        doReturn(commentPage)
                .when(commentRepository)
                .findByTilUuid(til.getUuid(), pageable);

        // When
        final CommentListReadResponseDto result = commentService.readCommentList(til.getUuid(), pageable);

        // Then
        assertThat(result.getComments()).isEmpty();
        assertThat(result.getTotalPages()).isEqualTo(commentPage.getTotalPages());
    }

    @Test
    @DisplayName("[Success] comments do exist")
    void successIfCommentsDoExist() {
        // Given
        doReturn(Optional.of(til))
                .when(tilRepository)
                .findByUuid(til.getUuid());

        final Pageable pageable = PageRequest.of(0, 5, Sort.by("createdDate").descending());

        final List<Comment> comments = List.of(CommentFactory.createDefaultComment(profile, til));

        final Page<Comment> commentPage = new PageImpl<>(comments, pageable, comments.size());

        doReturn(commentPage)
                .when(commentRepository)
                .findByTilUuid(til.getUuid(), pageable);

        // When
        final CommentListReadResponseDto result = commentService.readCommentList(til.getUuid(), pageable);

        // Then
        assertThat(result.getComments().get(0).getUuid()).hasSize(36);
        assertThat(result.getComments().get(0).getNickname()).isEqualTo(comments.get(0).getProfile().getNickname());
        assertThat(result.getComments().get(0).getContent()).isEqualTo(comments.get(0).getContent());
        assertThat(result.getTotalPages()).isEqualTo(commentPage.getTotalPages());
    }
}
