package me.tiary.service.profileservice;

import common.annotation.service.ServiceTest;
import common.config.factory.FactoryPreset;
import common.factory.domain.ProfileFactory;
import common.factory.dto.profile.ProfileUpdateRequestDtoFactory;
import me.tiary.domain.Profile;
import me.tiary.dto.profile.ProfileUpdateRequestDto;
import me.tiary.dto.profile.ProfileUpdateResponseDto;
import me.tiary.exception.ProfileException;
import me.tiary.exception.status.ProfileStatus;
import me.tiary.repository.ProfileRepository;
import me.tiary.service.ProfileService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.modelmapper.ModelMapper;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

@ServiceTest
@DisplayName("[ProfileService] updateProfile")
class UpdateProfileTest {
    @InjectMocks
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
    }

    @Test
    @DisplayName("[Fail] profile does not exist")
    void failIfProfileDoesNotExist() {
        // Given
        final String profileUuid = UUID.randomUUID().toString();

        doReturn(Optional.empty())
                .when(profileRepository)
                .findByUuid(any(String.class));

        final ProfileUpdateRequestDto requestDto = ProfileUpdateRequestDtoFactory.createDefaultProfileUpdateRequestDto();

        // When, Then
        final ProfileException result = assertThrows(ProfileException.class, () -> profileService.updateProfile(profileUuid, requestDto));

        assertThat(result.getStatus()).isEqualTo(ProfileStatus.NOT_EXISTING_PROFILE);
    }

    @Test
    @DisplayName("[Fail] nickname does exist")
    void failIfNicknameDoesExist() {
        // Given
        final String profileUuid = UUID.randomUUID().toString();

        final Profile profile = ProfileFactory.createDefaultProfile();

        doReturn(Optional.of(profile))
                .when(profileRepository)
                .findByUuid(profileUuid);

        doReturn(Optional.of(profile))
                .when(profileRepository)
                .findByNickname(profile.getNickname());

        final ProfileUpdateRequestDto requestDto = ProfileUpdateRequestDtoFactory.create(FactoryPreset.NICKNAME);

        // When, Then
        final ProfileException result = assertThrows(ProfileException.class, () -> profileService.updateProfile(profileUuid, requestDto));

        assertThat(result.getStatus()).isEqualTo(ProfileStatus.EXISTING_NICKNAME);
    }

    @Test
    @DisplayName("[Success] profile is acceptable")
    void successIfProfileIsAcceptable() {
        // Given
        final String profileUuid = UUID.randomUUID().toString();

        final Profile profile = ProfileFactory.createDefaultProfile();

        doReturn(Optional.of(profile))
                .when(profileRepository)
                .findByUuid(profileUuid);

        doReturn(Optional.empty())
                .when(profileRepository)
                .findByNickname(any(String.class));

        final ProfileUpdateRequestDto requestDto = ProfileUpdateRequestDtoFactory.createDefaultProfileUpdateRequestDto();

        // When
        final ProfileUpdateResponseDto result = profileService.updateProfile(profileUuid, requestDto);

        // Then
        assertThat(result.getNickname()).isEqualTo(profile.getNickname());
    }
}