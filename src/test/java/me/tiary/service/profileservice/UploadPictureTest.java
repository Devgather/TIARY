package me.tiary.service.profileservice;

import annotation.service.ServiceTest;
import factory.mock.web.MockMultipartFileFactory;
import me.tiary.exception.ProfileException;
import me.tiary.exception.status.ProfileStatus;
import me.tiary.service.ProfileService;
import me.tiary.utility.aws.AwsS3Manager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.modelmapper.ModelMapper;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

@ServiceTest
@DisplayName("[ProfileService] uploadPicture")
class UploadPictureTest {
    @InjectMocks
    private ProfileService profileService;

    @Mock
    private AwsS3Manager awsS3Manager;

    @Spy
    private ModelMapper modelMapper;

    @BeforeEach
    void beforeEach() {
        modelMapper.getConfiguration()
                .setFieldMatchingEnabled(true)
                .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE);
    }

    @Test
    @DisplayName("[Fail] content type is null")
    void failIfContentTypeIsNull() {
        // Given
        final String uuid = UUID.randomUUID().toString();

        final MultipartFile multipartFile = MockMultipartFileFactory.create("image", "image.png", null, 1);

        // When
        final ProfileException result = assertThrows(ProfileException.class, () -> profileService.uploadPicture(uuid, multipartFile));

        // Then
        assertThat(result.getStatus()).isEqualTo(ProfileStatus.NOT_EXISTING_CONTENT_TYPE);
    }

    @Test
    @DisplayName("[Fail] content type is improper")
    void failIfContentTypeIsImproper() {
        // Given
        final String uuid = UUID.randomUUID().toString();

        final MultipartFile multipartFile = MockMultipartFileFactory.createDefaultTxtMockMultipartFile(1);

        // When
        final ProfileException result = assertThrows(ProfileException.class, () -> profileService.uploadPicture(uuid, multipartFile));

        // Then
        assertThat(result.getStatus()).isEqualTo(ProfileStatus.NOT_SUPPORTING_CONTENT_TYPE);
    }

    @Test
    @DisplayName("[Success] picture is acceptable")
    void successIfPictureIsAcceptable() {
        // Given
        final MultipartFile multipartFile = MockMultipartFileFactory.createDefaultPngMockMultipartFile(1);

        final String profileUuid = UUID.randomUUID().toString();

        final Function<String, String> titleGenerator = ProfileService.createPictureTitleGenerator(profileUuid);

        final List<String> fileNameList = List.of(titleGenerator.apply(multipartFile.getOriginalFilename()));

        doReturn(fileNameList)
                .when(awsS3Manager)
                .uploadFiles(any(), any());

        // When
        final List<String> result = profileService.uploadPicture(profileUuid, multipartFile);

        // Then
        assertThat(result.size()).isEqualTo(1);
        assertThat(result.get(0)).isEqualTo(fileNameList.get(0));
    }
}