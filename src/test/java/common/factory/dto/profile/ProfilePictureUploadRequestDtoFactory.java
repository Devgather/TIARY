package common.factory.dto.profile;

import common.factory.mock.web.MockMultipartFileFactory;
import me.tiary.dto.profile.ProfilePictureUploadRequestDto;
import org.springframework.web.multipart.MultipartFile;

public final class ProfilePictureUploadRequestDtoFactory {
    public static ProfilePictureUploadRequestDto createDefaultProfilePictureUploadRequestDto() {
        return create(MockMultipartFileFactory.createDefaultPngMockMultipartFile(1));
    }

    public static ProfilePictureUploadRequestDto create(final MultipartFile pictureFile) {
        return ProfilePictureUploadRequestDto.builder()
                .pictureFile(pictureFile)
                .build();
    }
}