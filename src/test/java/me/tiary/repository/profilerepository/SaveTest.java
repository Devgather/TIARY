package me.tiary.repository.profilerepository;

import me.tiary.domain.Profile;
import me.tiary.repository.ProfileRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;
import utility.StringUtility;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DisplayName("[ProfileRepository] save")
class SaveTest {
    @Autowired
    private ProfileRepository profileRepository;

    @Test
    @DisplayName("[Fail] nickname is null")
    void failIfNicknameIsNull() {
        // Given
        final Profile profile = Profile.builder()
                .nickname(null)
                .picture("https://example.com/")
                .build();

        // Then
        assertThrows(DataIntegrityViolationException.class, () -> profileRepository.save(profile));
    }

    @Test
    @DisplayName("[Fail] nickname exceeds max length")
    void failIfNicknameExceedsMaxLength() {
        // Given
        final Profile profile = Profile.builder()
                .nickname(StringUtility.generateRandomString(Profile.NICKNAME_MAX_LENGTH + 1))
                .picture("https://example.com/")
                .build();

        // Then
        assertThrows(DataIntegrityViolationException.class, () -> profileRepository.save(profile));
    }

    @Test
    @DisplayName("[Fail] nickname is duplicated")
    void failIfNicknameIsDuplicated() {
        // Given
        final Profile profile1 = Profile.builder()
                .nickname("Test")
                .picture("https://example.com/")
                .build();

        final Profile profile2 = Profile.builder()
                .nickname("Test")
                .picture("https://example.com/")
                .build();

        profileRepository.save(profile1);

        // Then
        assertThrows(DataIntegrityViolationException.class, () -> profileRepository.save(profile2));
    }

    @Test
    @DisplayName("[Fail] picture is null")
    void failIfPictureIsNull() {
        // Given
        final Profile profile = Profile.builder()
                .nickname("Test")
                .picture(null)
                .build();

        // Then
        assertThrows(DataIntegrityViolationException.class, () -> profileRepository.save(profile));
    }

    @Test
    @DisplayName("[Success] profile is acceptable")
    void successIfProfileIsAcceptable() {
        // Given
        final Profile profile = Profile.builder()
                .nickname("Test")
                .picture("https://example.com/")
                .build();

        // When
        final Profile result = profileRepository.save(profile);

        // Then
        assertThat(result.getId()).isNotNull();
        assertThat(result.getNickname()).isEqualTo("Test");
        assertThat(result.getPicture()).isEqualTo("https://example.com/");
    }
}
