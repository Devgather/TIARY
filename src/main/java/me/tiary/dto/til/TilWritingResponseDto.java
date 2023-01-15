package me.tiary.dto.til;

import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
@Builder
@RequiredArgsConstructor
@Getter
public class TilWritingResponseDto {
    private final String uuid;
}