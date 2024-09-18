package me.tiary.service.commentservice;

import common.annotation.service.ServiceTest;
import common.factory.domain.CommentFactory;
import common.factory.domain.ProfileFactory;
import common.factory.domain.TilFactory;
import common.factory.dto.comment.CommentWritingRequestDtoFactory;
import me.tiary.domain.Comment;
import me.tiary.domain.Profile;
import me.tiary.domain.Til;
import me.tiary.dto.comment.CommentWritingRequestDto;
import me.tiary.dto.comment.CommentWritingResponseDto;
import me.tiary.exception.CommentException;
import me.tiary.exception.status.CommentStatus;
import me.tiary.repository.CommentRepository;
import me.tiary.repository.ProfileRepository;
import me.tiary.repository.TilRepository;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

@ServiceTest
@DisplayName("[CommentService] writeComment")
class WriteCommentTest {
    @InjectMocks
    private CommentService commentService;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private ProfileRepository profileRepository;

    @Mock
    private TilRepository tilRepository;

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
    @DisplayName("[Fail] profile does not exist")
    void failIfProfileDoesNotExist() {
        // Given
        final String profileUuid = UUID.randomUUID().toString();

        doReturn(Optional.empty())
                .when(profileRepository)
                .findByUuid(profileUuid);

        final String tilUuid = UUID.randomUUID().toString();

        final CommentWritingRequestDto requestDto = CommentWritingRequestDtoFactory.createDefaultCommentWritingRequestDto(tilUuid);

        // When, Then
        final CommentException result = assertThrows(CommentException.class, () -> commentService.writeComment(profileUuid, requestDto));

        assertThat(result.getStatus()).isEqualTo(CommentStatus.NOT_EXISTING_PROFILE);
    }

    @Test
    @DisplayName("[Fail] til does not exist")
    void failIfTilDoesNotExist() {
        // Given
        final String profileUuid = UUID.randomUUID().toString();

        doReturn(Optional.of(profile))
                .when(profileRepository)
                .findByUuid(profileUuid);

        final String tilUuid = UUID.randomUUID().toString();

        doReturn(Optional.empty())
                .when(tilRepository)
                .findByUuid(tilUuid);

        final CommentWritingRequestDto requestDto = CommentWritingRequestDtoFactory.createDefaultCommentWritingRequestDto(tilUuid);

        // When, Then
        final CommentException result = assertThrows(CommentException.class, () -> commentService.writeComment(profileUuid, requestDto));

        assertThat(result.getStatus()).isEqualTo(CommentStatus.NOT_EXISTING_TIL);
    }

    @Test
    @DisplayName("[Success] comment is acceptable")
    void successIfCommentIsAcceptable() {
        // Given
        final String profileUuid = UUID.randomUUID().toString();

        doReturn(Optional.of(profile))
                .when(profileRepository)
                .findByUuid(profileUuid);

        final String tilUuid = UUID.randomUUID().toString();

        doReturn(Optional.of(til))
                .when(tilRepository)
                .findByUuid(tilUuid);

        doReturn(CommentFactory.createDefaultComment(profile, til))
                .when(commentRepository)
                .save(any(Comment.class));

        final CommentWritingRequestDto requestDto = CommentWritingRequestDtoFactory.createDefaultCommentWritingRequestDto(tilUuid);

        // When
        final CommentWritingResponseDto result = commentService.writeComment(profileUuid, requestDto);

        // Then
        assertThat(result.getUuid()).hasSize(36);
    }
}