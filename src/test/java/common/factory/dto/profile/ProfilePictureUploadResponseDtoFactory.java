package common.factory.dto.profile;

import common.config.factory.FactoryPreset;
import common.factory.mock.web.MockMultipartFileFactory;
import me.tiary.dto.profile.ProfilePictureUploadResponseDto;
import me.tiary.service.ProfileService;
import org.springframework.mock.web.MockMultipartFile;

import java.util.UUID;
import java.util.function.Function;

public final class ProfilePictureUploadResponseDtoFactory {
    public static ProfilePictureUploadResponseDto createDefaultProfilePictureUploadResponseDto() {
        final MockMultipartFile multipartFile = MockMultipartFileFactory.createDefaultPngMockMultipartFile(1);

        final String profileUuid = UUID.randomUUID().toString();

        final Function<String, String> titleGenerator = ProfileService.createPictureTitleGenerator(profileUuid);

        final String picturePath = titleGenerator.apply(multipartFile.getOriginalFilename());

        return create(FactoryPreset.STORAGE + picturePath);
    }

    public static ProfilePictureUploadResponseDto create(final String picture) {
        return ProfilePictureUploadResponseDto.builder()
                .picture(picture)
                .build();
    }
}