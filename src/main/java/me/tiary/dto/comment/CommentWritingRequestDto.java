package me.tiary.dto.comment;

import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
@RequiredArgsConstructor
@Builder
@Getter
public class CommentWritingRequestDto {
    private final String tilUuid;

    private final String content;
}