package me.tiary.service.profileservice;

import common.annotation.service.ServiceTest;
import common.config.factory.FactoryPreset;
import common.factory.domain.ProfileFactory;
import common.factory.dto.profile.ProfilePictureUploadRequestDtoFactory;
import common.factory.mock.web.MockMultipartFileFactory;
import me.tiary.domain.Profile;
import me.tiary.dto.profile.ProfilePictureUploadRequestDto;
import me.tiary.dto.profile.ProfilePictureUploadResponseDto;
import me.tiary.exception.ProfileException;
import me.tiary.exception.status.ProfileStatus;
import me.tiary.properties.aws.AwsStorageProperties;
import me.tiary.repository.ProfileRepository;
import me.tiary.service.ProfileService;
import me.tiary.utility.aws.AwsS3Manager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Spy;
import org.modelmapper.ModelMapper;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

@ServiceTest
@DisplayName("[ProfileService] uploadPicture")
class UploadPictureTest {
    private ProfileService profileService;

    @Mock
    private ProfileRepository profileRepository;

    @Mock
    private AwsS3Manager awsS3Manager;

    @Spy
    private ModelMapper modelMapper;

    private AwsStorageProperties awsStorageProperties;

    @BeforeEach
    void beforeEach() {
        modelMapper.getConfiguration()
                .setFieldMatchingEnabled(true)
                .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE);

        awsStorageProperties = new AwsStorageProperties(FactoryPreset.PICTURE);

        profileService = new ProfileService(
                profileRepository,
                awsS3Manager,
                awsStorageProperties,
                modelMapper
        );
    }

    @Test
    @DisplayName("[Fail] profile does not exist")
    void failIfProfileDoesNotExist() {
        // Given
        final String profileUuid = UUID.randomUUID().toString();

        doReturn(Optional.empty())
                .when(profileRepository)
                .findByUuid(any(String.class));

        final ProfilePictureUploadRequestDto requestDto = ProfilePictureUploadRequestDtoFactory.createDefaultProfilePictureUploadRequestDto();

        // When
        final ProfileException result = assertThrows(ProfileException.class, () -> profileService.uploadPicture(profileUuid, requestDto));

        // Then
        assertThat(result.getStatus()).isEqualTo(ProfileStatus.NOT_EXISTING_PROFILE);
    }

    @Test
    @DisplayName("[Fail] content type is null")
    void failIfContentTypeIsNull() {
        // Given
        final String profileUuid = UUID.randomUUID().toString();

        doReturn(Optional.of(ProfileFactory.createDefaultProfile()))
                .when(profileRepository)
                .findByUuid(any(String.class));

        final MultipartFile multipartFile = MockMultipartFileFactory.create("image", "image.png", null, 1);

        final ProfilePictureUploadRequestDto requestDto = ProfilePictureUploadRequestDtoFactory.create(multipartFile);

        // When
        final ProfileException result = assertThrows(ProfileException.class, () -> profileService.uploadPicture(profileUuid, requestDto));

        // Then
        assertThat(result.getStatus()).isEqualTo(ProfileStatus.NOT_EXISTING_CONTENT_TYPE);
    }

    @Test
    @DisplayName("[Fail] content type is improper")
    void failIfContentTypeIsImproper() {
        // Given
        final String profileUuid = UUID.randomUUID().toString();

        doReturn(Optional.of(ProfileFactory.createDefaultProfile()))
                .when(profileRepository)
                .findByUuid(any(String.class));

        final MultipartFile multipartFile = MockMultipartFileFactory.createDefaultTxtMockMultipartFile(1);

        final ProfilePictureUploadRequestDto requestDto = ProfilePictureUploadRequestDtoFactory.create(multipartFile);

        // When
        final ProfileException result = assertThrows(ProfileException.class, () -> profileService.uploadPicture(profileUuid, requestDto));

        // Then
        assertThat(result.getStatus()).isEqualTo(ProfileStatus.NOT_SUPPORTING_CONTENT_TYPE);
    }

    @Test
    @DisplayName("[Success] picture is acceptable")
    void successIfPictureIsAcceptable() {
        // Given
        final String profileUuid = UUID.randomUUID().toString();

        final Profile profile = ProfileFactory.createDefaultProfile();

        doReturn(Optional.of(profile))
                .when(profileRepository)
                .findByUuid(profileUuid);

        final ProfilePictureUploadRequestDto requestDto = ProfilePictureUploadRequestDtoFactory.createDefaultProfilePictureUploadRequestDto();

        final Function<String, String> titleGenerator = ProfileService.createPictureTitleGenerator(profileUuid);

        final List<String> picturePathList = List.of(titleGenerator.apply(requestDto.getPictureFile().getOriginalFilename()));

        doReturn(picturePathList)
                .when(awsS3Manager)
                .uploadFiles(any(), any());

        // When
        final ProfilePictureUploadResponseDto result = profileService.uploadPicture(profileUuid, requestDto);

        // Then
        assertThat(result.getPicture()).isEqualTo(profile.getPicture());
    }
}