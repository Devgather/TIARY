package me.tiary.domain.oauth;

import me.tiary.domain.OAuth;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("[OAuth] createUuid")
class CreateUuidTest {
    @Test
    @DisplayName("[Success] uuid is created")
    void successIfUuidIsCreated() {
        // Given
        final OAuth oAuth = OAuth.builder().build();

        // When
        oAuth.createUuid();

        // Then
        assertThat(oAuth.getUuid()).isNotNull();
        assertThat(oAuth.getUuid().length()).isEqualTo(36);
    }
}