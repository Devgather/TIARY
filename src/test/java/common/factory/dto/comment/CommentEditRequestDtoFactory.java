package common.factory.dto.comment;

import common.config.factory.FactoryPreset;
import me.tiary.dto.comment.CommentEditRequestDto;

public final class CommentEditRequestDtoFactory {
    public static CommentEditRequestDto createDefaultCommentEditRequestDto() {
        return create(FactoryPreset.CONTENT);
    }

    public static CommentEditRequestDto create(final String content) {
        return CommentEditRequestDto.builder()
                .content(content)
                .build();
    }
}
