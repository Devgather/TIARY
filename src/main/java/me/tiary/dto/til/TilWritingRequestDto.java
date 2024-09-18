package me.tiary.dto.til;

import lombok.*;

import javax.validation.constraints.NotBlank;

@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
@Getter
@Builder
@RequiredArgsConstructor
@EqualsAndHashCode
public class TilWritingRequestDto {
    @NotBlank
    private final String title;

    @NotBlank
    private final String content;
}