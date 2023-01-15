package me.tiary.dto.til;

import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
@Builder
@RequiredArgsConstructor
@Getter
public class TilEditResponseDto {
    private final String tilUuid;
}