package me.tiary.dto.profile;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
@RequiredArgsConstructor
@Builder
public class ProfileReadResponseDto {
    private final String nickname;

    private final String picture;
}