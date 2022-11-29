package me.tiary.domain.verification;

import me.tiary.domain.Verification;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("[Verification] createUuid")
class CreateUuidTest {
    @Test
    @DisplayName("[Success] uuid is created")
    void successIfUuidIsCreated() {
        // Given
        final Verification verification = Verification.builder().build();

        // When
        verification.createUuid();

        // Then
        assertThat(verification.getUuid()).isNotNull();
        assertThat(verification.getUuid().length()).isEqualTo(36);
    }
}