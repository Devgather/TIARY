package me.tiary.dto.comment;

import lombok.*;
import me.tiary.vo.comment.CommentWithProfileVo;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
@RequiredArgsConstructor
@Builder
@Getter
public class CommentListReadResponseDto {
    private final List<CommentWithProfileVo> comments;

    private final int totalPages;
}