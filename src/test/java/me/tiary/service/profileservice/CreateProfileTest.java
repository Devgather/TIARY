package me.tiary.service.profileservice;

import common.annotation.service.ServiceTest;
import common.config.factory.FactoryPreset;
import common.factory.domain.ProfileFactory;
import common.factory.dto.profile.ProfileCreationRequestDtoFactory;
import me.tiary.domain.Profile;
import me.tiary.dto.profile.ProfileCreationRequestDto;
import me.tiary.dto.profile.ProfileCreationResponseDto;
import me.tiary.exception.ProfileException;
import me.tiary.exception.status.ProfileStatus;
import me.tiary.properties.aws.AwsStorageProperties;
import me.tiary.repository.ProfileRepository;
import me.tiary.service.ProfileService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Spy;
import org.modelmapper.ModelMapper;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;

@ServiceTest
@DisplayName("[ProfileService] createProfile")
class CreateProfileTest {
    private ProfileService profileService;

    @Mock
    private ProfileRepository profileRepository;

    @Spy
    private ModelMapper modelMapper;

    @BeforeEach
    void beforeEach() {
        modelMapper.getConfiguration()
                .setFieldMatchingEnabled(true)
                .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE);

        final AwsStorageProperties awsStorageProperties = new AwsStorageProperties(FactoryPreset.STORAGE);

        profileService = new ProfileService(
                profileRepository, null, awsStorageProperties, modelMapper
        );
    }

    @Test
    @DisplayName("[Fail] nickname does exist")
    void failIfNicknameDoesExist() {
        // Given
        final Profile profile = ProfileFactory.createDefaultProfile();

        doReturn(Optional.ofNullable(profile))
                .when(profileRepository)
                .findByNickname(eq(profile.getNickname()));

        final ProfileCreationRequestDto requestDto = ProfileCreationRequestDtoFactory.createDefaultProfileCreationRequestDto();

        // When, Then
        final ProfileException result = assertThrows(ProfileException.class, () -> profileService.createProfile(requestDto));

        assertThat(result.getStatus()).isEqualTo(ProfileStatus.EXISTING_NICKNAME);
    }

    @Test
    @DisplayName("[Success] profile is acceptable")
    void successIfProfileIsAcceptable() {
        // Given
        doReturn(Optional.empty())
                .when(profileRepository)
                .findByNickname(any(String.class));

        doReturn(ProfileFactory.createDefaultProfile())
                .when(profileRepository)
                .save(any(Profile.class));

        final ProfileCreationRequestDto requestDto = ProfileCreationRequestDtoFactory.createDefaultProfileCreationRequestDto();

        // When
        final ProfileCreationResponseDto result = profileService.createProfile(requestDto);

        // Then
        assertThat(result.getUuid().length()).isEqualTo(36);
        assertThat(result.getNickname()).isEqualTo(requestDto.getNickname());
    }
}
