package me.tiary.dto.profile;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
@RequiredArgsConstructor
@Builder
public class ProfilePictureUploadRequestDto {
    private final MultipartFile pictureFile;
}
