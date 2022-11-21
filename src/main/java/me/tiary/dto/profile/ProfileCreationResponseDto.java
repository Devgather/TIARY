package me.tiary.dto.profile;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
public class ProfileCreationResponseDto {
    private final Long id;

    private final String nickname;
}
