package me.tiary.service.profileservice;

import me.tiary.domain.Profile;
import me.tiary.repository.ProfileRepository;
import me.tiary.service.ProfileService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
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
        final boolean result = profileService.checkNicknameDuplication("Test");

        // Then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("[Success] nickname does exist")
    void successIfNicknameDoesExist() {
        // Given
        doReturn(Optional.ofNullable(Profile.builder().build()))
                .when(profileRepository)
                .findByNickname(eq("Test"));

        // When
        final boolean result = profileService.checkNicknameDuplication("Test");

        // Then
        assertThat(result).isTrue();
    }
}