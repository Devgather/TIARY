package common.factory.vo.comment;

import common.config.factory.FactoryPreset;
import me.tiary.vo.comment.CommentWithProfileVo;

import java.time.LocalDateTime;
import java.util.UUID;

public final class CommentWithProfileVoFactory {
    public static CommentWithProfileVo createDefaultCommentWithProfileVo() {
        return create(UUID.randomUUID().toString(), FactoryPreset.NICKNAME, FactoryPreset.CONTENT, LocalDateTime.now());
    }

    public static CommentWithProfileVo create(final String uuid, final String nickname, final String content, final LocalDateTime createdDate) {
        return CommentWithProfileVo.builder()
                .uuid(uuid)
                .nickname(nickname)
                .content(content)
                .createdDate(createdDate)
                .build();
    }
}
