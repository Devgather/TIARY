package common.factory.dto.comment;

import common.config.factory.FactoryPreset;
import me.tiary.dto.comment.CommentWritingRequestDto;

public final class CommentWritingRequestDtoFactory {
    public static CommentWritingRequestDto createDefaultCommentWritingRequestDto(final String tilUuid) {
        return create(tilUuid, FactoryPreset.CONTENT);
    }

    public static CommentWritingRequestDto create(final String tilUuid, final String content) {
        return CommentWritingRequestDto.builder()
                .tilUuid(tilUuid)
                .content(content)
                .build();
    }
}
