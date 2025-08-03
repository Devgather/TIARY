package me.tiary.profile.service;

import me.tiary.profile.domain.ProfileFixture;
import me.tiary.profile.repository.ProfileRepository;
import me.tiary.support.annotation.ServiceTest;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ServiceTest
class ProfileServiceTests {

    @InjectMocks
    ProfileService profileService;

    @Mock
    ProfileRepository profileRepository;

    @Nested
    class IsNicknameAvailableTest {

        @Test
        void shouldDetermineNicknameIsUnavailable_whenNicknameDoesExist() {
            // Given
            given(profileRepository.existsByNickname(ProfileFixture.DEFAULT_NICKNAME))
                    .willReturn(true);

            // When
            boolean result = profileService.isNicknameAvailable(ProfileFixture.DEFAULT_NICKNAME);

            // Then
            assertThat(result).isFalse();
        }

        @Test
        void shouldDetermineNicknameIsAvailable_whenNicknameDoesNotExist() {
            // Given
            given(profileRepository.existsByNickname(ProfileFixture.DEFAULT_NICKNAME))
                    .willReturn(false);

            // When
            boolean result = profileService.isNicknameAvailable(ProfileFixture.DEFAULT_NICKNAME);

            // Then
            assertThat(result).isTrue();
        }

    }

}
