package common.factory.dto.comment;

import common.config.factory.FactoryPreset;
import me.tiary.dto.comment.CommentEditResponseDto;

public final class CommentEditResponseDtoFactory {
    public static CommentEditResponseDto createDefaultCommentEditResponseDto() {
        return create(FactoryPreset.CONTENT);
    }

    public static CommentEditResponseDto create(final String content) {
        return CommentEditResponseDto.builder()
                .content(content)
                .build();
    }
}
