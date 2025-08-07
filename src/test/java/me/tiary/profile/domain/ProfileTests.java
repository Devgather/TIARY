package me.tiary.profile.domain;

import me.tiary.profile.repository.ProfileRepository;
import me.tiary.support.annotation.EntityTest;
import me.tiary.support.util.lang.StringUtils;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@EntityTest
class ProfileTests {

    @Autowired
    ProfileRepository profileRepository;

    @Nested
    class ConstraintTest {

        @Test
        void shouldThrowDataIntegrityViolationException_whenNicknameExceedsMaxLength() {
            // Given
            Profile profile = ProfileFixture.of(StringUtils.generateRandomString(Profile.MAX_NICKNAME_LENGTH + 1), ProfileFixture.DEFAULT_PICTURE_URL);

            // When, Then
            assertThatThrownBy(() -> profileRepository.saveAndFlush(profile)).isInstanceOf(DataIntegrityViolationException.class);
        }

        @Test
        void shouldThrowDataIntegrityViolationException_whenNicknameIsNull() {
            // Given
            Profile profile = ProfileFixture.of(null, ProfileFixture.DEFAULT_PICTURE_URL);

            // When, Then
            assertThatThrownBy(() -> profileRepository.saveAndFlush(profile)).isInstanceOf(DataIntegrityViolationException.class);
        }

        @Test
        void shouldThrowDataIntegrityViolationException_whenNicknameIsDuplicated() {
            // Given
            Profile profile1 = ProfileFixture.create();
            Profile profile2 = ProfileFixture.create();

            profileRepository.saveAndFlush(profile1);

            // When, Then
            assertThatThrownBy(() -> profileRepository.saveAndFlush(profile2)).isInstanceOf(DataIntegrityViolationException.class);
        }

        @Test
        void shouldThrowDataIntegrityViolationException_whenPictureUrlIsNull() {
            // Given
            Profile profile = ProfileFixture.of(ProfileFixture.DEFAULT_NICKNAME, null);

            // When, Then
            assertThatThrownBy(() -> profileRepository.saveAndFlush(profile)).isInstanceOf(DataIntegrityViolationException.class);
        }

        @Test
        void shouldSaveSuccessfully_whenAllFieldsAreValid() {
            // Given
            Profile profile = ProfileFixture.create();

            // When
            Profile result = profileRepository.saveAndFlush(profile);

            // Then
            assertSoftly(softly -> {
                softly.assertThat(result.getId()).isNotNull();
                softly.assertThat(result.getNickname()).isEqualTo(ProfileFixture.DEFAULT_NICKNAME);
                softly.assertThat(result.getPictureUrl()).isEqualTo(ProfileFixture.DEFAULT_PICTURE_URL);
            });
        }

    }

}
