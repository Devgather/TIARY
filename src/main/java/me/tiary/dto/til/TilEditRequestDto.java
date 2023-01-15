package me.tiary.dto.til;

import lombok.*;

import javax.validation.constraints.NotBlank;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
@RequiredArgsConstructor
@Builder
@Getter
public class TilEditRequestDto {
    @NotBlank
    private final String title;

    @NotBlank
    private final String content;

    private final List<String> tags;
}