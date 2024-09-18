package me.tiary.domain.tag;

import me.tiary.domain.Tag;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("[Tag] createUuid")
class CreateUuidTest {
    @Test
    @DisplayName("[Success] uuid is created")
    void successIfUuidIsCreated() {
        // Given
        final Tag tag = Tag.builder().build();

        // When
        tag.createUuid();

        // Then
        assertThat(tag.getUuid()).isNotNull();
        assertThat(tag.getUuid()).hasSize(36);
    }
}