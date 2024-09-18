package me.tiary.dto.til;

import lombok.*;

import javax.validation.constraints.NotBlank;

@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
@RequiredArgsConstructor
@Builder
@Getter
@EqualsAndHashCode
public class TilEditRequestDto {
    @NotBlank
    private final String title;

    @NotBlank
    private final String content;
}