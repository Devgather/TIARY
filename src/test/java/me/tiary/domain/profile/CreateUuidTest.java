package me.tiary.domain.profile;

import me.tiary.domain.Profile;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("[Profile] createUuid")
class CreateUuidTest {
    @Test
    @DisplayName("[Success] uuid is created")
    void successIfUuidIsCreated() {
        // Given
        final Profile profile = Profile.builder().build();

        // When
        profile.createUuid();

        // Then
        assertThat(profile.getUuid()).isNotNull();
        assertThat(profile.getUuid().length()).isEqualTo(36);
    }
}