package me.tiary.service;

import lombok.RequiredArgsConstructor;
import me.tiary.domain.Comment;
import me.tiary.domain.Profile;
import me.tiary.domain.Til;
import me.tiary.dto.comment.CommentDeletionResponseDto;
import me.tiary.dto.comment.CommentListReadResponseDto;
import me.tiary.dto.comment.CommentWritingRequestDto;
import me.tiary.dto.comment.CommentWritingResponseDto;
import me.tiary.exception.CommentException;
import me.tiary.exception.status.CommentStatus;
import me.tiary.repository.CommentRepository;
import me.tiary.repository.ProfileRepository;
import me.tiary.repository.TilRepository;
import me.tiary.vo.comment.CommentWithProfileVo;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {
    private final CommentRepository commentRepository;

    private final ProfileRepository profileRepository;

    private final TilRepository tilRepository;

    private final ModelMapper modelMapper;

    @Transactional
    public CommentWritingResponseDto writeComment(final String profileUuid, final CommentWritingRequestDto requestDto) {
        final Profile profile = profileRepository.findByUuid(profileUuid)
                .orElseThrow(() -> new CommentException(CommentStatus.NOT_EXISTING_PROFILE));

        final Til til = tilRepository.findByUuid(requestDto.getTilUuid())
                .orElseThrow(() -> new CommentException(CommentStatus.NOT_EXISTING_TIL));

        final Comment comment = Comment.builder()
                .profile(profile)
                .til(til)
                .content(requestDto.getContent())
                .build();

        final Comment result = commentRepository.save(comment);

        return modelMapper.map(result, CommentWritingResponseDto.class);
    }

    public CommentListReadResponseDto readCommentList(final String tilUuid, final Pageable pageable) {
        if (tilRepository.findByUuid(tilUuid).isEmpty()) {
            throw new CommentException(CommentStatus.NOT_EXISTING_TIL);
        }

        final Page<Comment> commentPage = commentRepository.findByTilUuid(tilUuid, pageable);

        final List<Comment> commentContent = commentPage.getContent();

        final int totalPages = commentPage.getTotalPages();

        final List<CommentWithProfileVo> comments = new ArrayList<>();

        for (final Comment comment : commentContent) {
            final CommentWithProfileVo commentWithProfileVo = CommentWithProfileVo.builder()
                    .uuid(comment.getUuid())
                    .nickname(comment.getProfile().getNickname())
                    .content(comment.getContent())
                    .createdDate(comment.getCreatedDate())
                    .build();

            comments.add(commentWithProfileVo);
        }

        return CommentListReadResponseDto.builder()
                .comments(comments)
                .totalPages(totalPages)
                .build();
    }

    @Transactional
    public CommentDeletionResponseDto deleteComment(final String profileUuid, final String commentUuid) {
        final Comment comment = commentRepository.findByUuidJoinFetchProfile(commentUuid)
                .orElseThrow(() -> new CommentException(CommentStatus.NOT_EXISTING_COMMENT));

        if (!comment.getProfile().getUuid().equals(profileUuid)) {
            throw new CommentException(CommentStatus.NOT_AUTHORIZED_MEMBER);
        }

        commentRepository.delete(comment);

        return modelMapper.map(comment, CommentDeletionResponseDto.class);
    }
}