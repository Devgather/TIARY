package me.tiary.domain.profile;

import common.config.factory.FactoryPreset;
import common.factory.domain.ProfileFactory;
import me.tiary.domain.Profile;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("[Profile] updatePicture")
class UpdatePictureTest {
    @Test
    @DisplayName("[Success] profile picture is updated")
    void successIfProfilePictureIsUpdated() {
        // Given
        final Profile profile = ProfileFactory.createDefaultProfile();

        final String pictureUrl = FactoryPreset.STORAGE + FactoryPreset.PICTURE;

        // When
        profile.updatePicture(pictureUrl);

        // Then
        assertThat(profile.getPicture()).isEqualTo(pictureUrl);
    }
}
