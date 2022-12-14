package me.tiary.service.profileservice;

import annotation.service.ServiceTest;
import config.factory.FactoryPreset;
import factory.domain.ProfileFactory;
import me.tiary.domain.Profile;
import me.tiary.repository.ProfileRepository;
import me.tiary.service.ProfileService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;

@ServiceTest
@DisplayName("[ProfileService] checkNicknameDuplication")
class CheckNicknameDuplicationTest {
    @InjectMocks
    private ProfileService profileService;

    @Mock
    private ProfileRepository profileRepository;

    @Test
    @DisplayName("[Success] nickname does not exist")
    void successIfNicknameDoesNotExist() {
        // Given
        doReturn(Optional.empty())
                .when(profileRepository)
                .findByNickname(any(String.class));

        // When
        final boolean result = profileService.checkNicknameDuplication(FactoryPreset.NICKNAME);

        // Then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("[Success] nickname does exist")
    void successIfNicknameDoesExist() {
        // Given
        final Profile profile = ProfileFactory.createDefaultProfile();

        doReturn(Optional.ofNullable(profile))
                .when(profileRepository)
                .findByNickname(eq(profile.getNickname()));

        // When
        final boolean result = profileService.checkNicknameDuplication(FactoryPreset.NICKNAME);

        // Then
        assertThat(result).isTrue();
    }
}