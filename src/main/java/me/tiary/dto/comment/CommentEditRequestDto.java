package me.tiary.dto.comment;

import lombok.*;

import javax.validation.constraints.NotBlank;

@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
@RequiredArgsConstructor
@Builder
@Getter
@EqualsAndHashCode
public class CommentEditRequestDto {
    @NotBlank
    private final String content;
}
