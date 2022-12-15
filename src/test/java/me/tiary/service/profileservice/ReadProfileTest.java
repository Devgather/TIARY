package me.tiary.service.profileservice;

import annotation.service.ServiceTest;
import config.factory.FactoryPreset;
import factory.domain.ProfileFactory;
import me.tiary.domain.Profile;
import me.tiary.dto.profile.ProfileReadResponseDto;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;

@ServiceTest
@DisplayName("[ProfileService] readProfile")
class ReadProfileTest {
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
        doReturn(Optional.empty())
                .when(profileRepository)
                .findByNickname(any(String.class));

        // When, Then
        final ProfileException result = assertThrows(ProfileException.class, () -> profileService.readProfile(FactoryPreset.NICKNAME));

        assertThat(result.getStatus()).isEqualTo(ProfileStatus.NOT_EXISTING_PROFILE);
    }

    @Test
    @DisplayName("[Success] profile does exist")
    void successIfProfileDoesExist() {
        // Given
        final Profile profile = ProfileFactory.createDefaultProfile();

        doReturn(Optional.ofNullable(profile))
                .when(profileRepository)
                .findByNickname(eq(profile.getNickname()));

        // When
        final ProfileReadResponseDto result = profileService.readProfile(FactoryPreset.NICKNAME);

        // Then
        assertThat(result.getNickname()).isEqualTo(profile.getNickname());
        assertThat(result.getPicture()).isEqualTo(profile.getPicture());
    }
}