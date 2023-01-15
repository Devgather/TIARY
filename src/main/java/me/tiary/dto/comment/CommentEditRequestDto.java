package me.tiary.dto.comment;

import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
@RequiredArgsConstructor
@Builder
@Getter
public class CommentEditRequestDto {
    private final String content;
}
