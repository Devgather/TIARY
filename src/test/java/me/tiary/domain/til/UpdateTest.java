package me.tiary.domain.til;

import common.factory.domain.ProfileFactory;
import common.factory.domain.TilFactory;
import me.tiary.domain.Profile;
import me.tiary.domain.Til;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("[Til] update")
class UpdateTest {
    @Test
    @DisplayName("[Success] til is updated")
    void successIfTilIsUpdated() {
        // Given
        final Profile profile = ProfileFactory.createDefaultProfile();

        final Til til = TilFactory.createDefaultTil(profile);

        final String title = "New title";
        final String content = "New content";

        // When
        til.update(title, content);

        // Then
        assertThat(til.getTitle()).isEqualTo(title);
        assertThat(til.getContent()).isEqualTo(content);
    }
}