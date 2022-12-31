package me.tiary.dto.profile;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
@RequiredArgsConstructor
@Builder
public class ProfilePictureUploadResponseDto {
    private final String picture;
}
