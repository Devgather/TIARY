package me.tiary.repository.profilerepository;

import me.tiary.domain.Profile;
import me.tiary.repository.ProfileRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DisplayName("[ProfileRepository] findByNickname")
class FindByNicknameTest {
    @Autowired
    private ProfileRepository profileRepository;

    @Test
    @DisplayName("[Success] nickname does not exist")
    void successIfNicknameDoesNotExist() {
        // When
        final Optional<Profile> result = profileRepository.findByNickname("Test");

        // Then
        assertThat(result.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("[Success] nickname does exist")
    void successIfNicknameDoesExist() {
        // Given
        final Profile profile = Profile.builder()
                .nickname("Test")
                .picture("https://example.com/")
                .build();

        profileRepository.save(profile);

        // When
        final Optional<Profile> result = profileRepository.findByNickname("Test");

        // Then
        assertThat(result.isPresent()).isTrue();
        assertThat(result.get().getNickname()).isEqualTo("Test");
    }
}