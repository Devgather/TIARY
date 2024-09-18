package me.tiary.dto.profile;

import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
@RequiredArgsConstructor
@Builder
@Getter
public class ProfileUpdateResponseDto {
    private final String nickname;
}