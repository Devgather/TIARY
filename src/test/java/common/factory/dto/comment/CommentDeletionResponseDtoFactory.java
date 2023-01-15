package common.factory.dto.comment;

import me.tiary.dto.comment.CommentDeletionResponseDto;

import java.util.UUID;

public final class CommentDeletionResponseDtoFactory {
    public static CommentDeletionResponseDto createDefaultCommentDeletionResponseDto() {
        return create(UUID.randomUUID().toString());
    }

    public static CommentDeletionResponseDto create(final String uuid) {
        return CommentDeletionResponseDto.builder()
                .uuid(uuid)
                .build();
    }
}