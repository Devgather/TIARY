package me.tiary.domain.comment;

import me.tiary.domain.Comment;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("[Comment] createUuid")
class CreateUuidTest {
    @Test
    @DisplayName("[Success] uuid is created")
    void successIfUuidIsCreated() {
        // Given
        final Comment comment = Comment.builder().build();

        // When
        comment.createUuid();

        // Then
        assertThat(comment.getUuid()).isNotNull();
        assertThat(comment.getUuid().length()).isEqualTo(36);
    }
}