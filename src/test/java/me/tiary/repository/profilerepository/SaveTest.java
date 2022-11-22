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
                .build();
        final Profile profile2 = Profile.builder()
                .nickname("Test")
                .build();
        profileRepository.save(profile1);

        // Then
        assertThrows(DataIntegrityViolationException.class, () -> profileRepository.save(profile2));
    }

    @Test
    @DisplayName("[Success] profile is acceptable")
    void successIfProfileIsAcceptable() {
        // Given
        final Profile profile = Profile.builder()
                .nickname("Test")
                .build();

        // When
        final Profile result = profileRepository.save(profile);

        // Then
        assertThat(result.getId()).isNotNull();
        assertThat(result.getNickname()).isEqualTo("Test");
    }
}
