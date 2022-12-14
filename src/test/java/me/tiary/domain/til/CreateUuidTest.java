package me.tiary.domain.til;

import me.tiary.domain.Til;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("[Til] createUuid")
class CreateUuidTest {
    @Test
    @DisplayName("[Success] uuid is created")
    void successIfUuidIsCreated() {
        // Given
        final Til til = Til.builder().build();

        // When
        til.createUuid();

        // Then
        assertThat(til.getUuid()).isNotNull();
        assertThat(til.getUuid().length()).isEqualTo(36);
    }
}