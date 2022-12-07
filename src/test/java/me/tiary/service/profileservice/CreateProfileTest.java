package me.tiary.service.profileservice;

import annotation.service.ServiceTest;
import me.tiary.domain.Profile;
import me.tiary.dto.profile.ProfileCreationRequestDto;
import me.tiary.dto.profile.ProfileCreationResponseDto;
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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;

@ServiceTest
@DisplayName("[ProfileService] createProfile")
class CreateProfileTest {
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
    @DisplayName("[Fail] nickname does exist")
    void failIfNicknameDoesExist() {
        // Given
        final ProfileCreationRequestDto requestDto = ProfileCreationRequestDto.builder()
                .nickname("Test")
                .build();

        doReturn(Optional.ofNullable(Profile.builder().build()))
                .when(profileRepository)
                .findByNickname(eq(requestDto.getNickname()));

        // When
        final ProfileException result = assertThrows(ProfileException.class, () -> profileService.createProfile(requestDto));

        // Then
        assertThat(result.getStatus()).isEqualTo(ProfileStatus.EXISTING_NICKNAME);
    }

    @Test
    @DisplayName("[Success] profile is acceptable")
    void successIfProfileIsAcceptable() {
        // Given
        final String uuid = UUID.randomUUID().toString();

        final ProfileCreationRequestDto requestDto = ProfileCreationRequestDto.builder()
                .nickname("Test")
                .build();

        doReturn(Optional.empty())
                .when(profileRepository)
                .findByNickname(eq(requestDto.getNickname()));

        final Profile savedProfile = Profile.builder()
                .id(1L)
                .uuid(uuid)
                .nickname("Test")
                .picture("https://example.com/")
                .build();

        doReturn(savedProfile)
                .when(profileRepository)
                .save(any(Profile.class));

        // When
        final ProfileCreationResponseDto result = profileService.createProfile(requestDto);

        // Then
        assertThat(result.getUuid().length()).isEqualTo(36);
        assertThat(result.getNickname()).isEqualTo(requestDto.getNickname());
    }
}
