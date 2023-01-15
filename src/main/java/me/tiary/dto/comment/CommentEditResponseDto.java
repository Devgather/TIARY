package me.tiary.dto.comment;

import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
@RequiredArgsConstructor
@Builder
@Getter
public class CommentEditResponseDto {
    private final String content;
}
