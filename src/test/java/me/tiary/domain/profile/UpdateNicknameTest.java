package me.tiary.domain.profile;

import common.factory.domain.ProfileFactory;
import me.tiary.domain.Profile;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("[Profile] updateNickname")
class UpdateNicknameTest {
    @Test
    @DisplayName("[Success] profile nickname is updated")
    void successIfProfileNicknameIsUpdated() {
        // Given
        final Profile profile = ProfileFactory.createDefaultProfile();

        final String nickname = "updatedNickname";

        // When
        profile.updateNickname(nickname);

        // Then
        assertThat(profile.getNickname()).isEqualTo(nickname);
    }
}
