package me.tiary.repository.profilerepository;

import annotation.repository.RepositoryIntegrationTest;
import me.tiary.domain.Profile;
import me.tiary.repository.ProfileRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import utility.JpaUtility;
import utility.StringUtility;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@RepositoryIntegrationTest
@DisplayName("[ProfileRepository - Integration] save")
class SaveIntegrationTest {
    @Autowired
    private ProfileRepository profileRepository;

    @PersistenceContext
    private EntityManager em;

    @Test
    @DisplayName("[Fail] nickname is null")
    void failIfNicknameIsNull() {
        // Given
        final Profile profile = Profile.builder()
                .nickname(null)
                .picture("https://example.com/")
                .build();

        // When, Then
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

        // When, Then
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

        JpaUtility.flushAndClear(em);

        // When, Then
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

        // When, Then
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
        assertThat(result.getUuid().length()).isEqualTo(36);
        assertThat(result.getNickname()).isEqualTo(profile.getNickname());
        assertThat(result.getPicture()).isEqualTo(profile.getPicture());
    }
}
