package me.tiary.vo.comment;

import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
@RequiredArgsConstructor
@Builder
@Getter
public class CommentWithProfileVo {
    private final String uuid;

    private final String nickname;

    private final String content;

    private final LocalDateTime createdDate;
}
