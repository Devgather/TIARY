package me.tiary.dto.profile;

import lombok.*;
import me.tiary.domain.Profile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
@RequiredArgsConstructor
@Builder
public class ProfileCreationRequestDto {
    @NotBlank
    @Size(max = Profile.NICKNAME_MAX_LENGTH)
    private final String nickname;
}
