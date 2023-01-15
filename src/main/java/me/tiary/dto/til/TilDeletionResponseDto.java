package me.tiary.dto.til;

import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
@RequiredArgsConstructor
@Builder
@Getter
public class TilDeletionResponseDto {
    private final String uuid;
}