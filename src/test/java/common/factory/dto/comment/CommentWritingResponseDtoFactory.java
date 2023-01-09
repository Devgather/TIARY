package common.factory.dto.comment;

import me.tiary.dto.comment.CommentWritingResponseDto;

import java.util.UUID;

public final class CommentWritingResponseDtoFactory {
    public static CommentWritingResponseDto createDefaultCommentWritingResponseDto() {
        return create(UUID.randomUUID().toString());
    }

    public static CommentWritingResponseDto create(final String uuid) {
        return CommentWritingResponseDto.builder()
                .uuid(uuid)
                .build();
    }
}
