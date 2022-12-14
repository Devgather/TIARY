package me.tiary.domain.tiltag;

import me.tiary.domain.TilTag;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("[TilTag] createUuid")
class CreateUuidTest {
    @Test
    @DisplayName("[Success] uuid is created")
    void successIfUuidIsCreated() {
        // Given
        final TilTag tilTag = TilTag.builder().build();

        // When
        tilTag.createUuid();

        // Then
        assertThat(tilTag.getUuid()).isNotNull();
        assertThat(tilTag.getUuid().length()).isEqualTo(36);
    }
}