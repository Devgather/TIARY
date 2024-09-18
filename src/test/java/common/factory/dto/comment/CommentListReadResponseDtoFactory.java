package common.factory.dto.comment;

import common.factory.vo.comment.CommentWithProfileVoFactory;
import me.tiary.dto.comment.CommentListReadResponseDto;
import me.tiary.vo.comment.CommentWithProfileVo;

import java.util.List;

public final class CommentListReadResponseDtoFactory {
    public static CommentListReadResponseDto createDefaultCommentListReadResponseDto() {
        return create(List.of(CommentWithProfileVoFactory.createDefaultCommentWithProfileVo()), 1);
    }

    public static CommentListReadResponseDto create(final List<CommentWithProfileVo> comments, final int totalPages) {
        return CommentListReadResponseDto.builder()
                .comments(comments)
                .totalPages(totalPages)
                .build();
    }
}
