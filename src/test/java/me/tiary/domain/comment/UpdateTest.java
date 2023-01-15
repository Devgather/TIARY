package me.tiary.domain.comment;

import common.factory.domain.CommentFactory;
import common.factory.domain.ProfileFactory;
import common.factory.domain.TilFactory;
import me.tiary.domain.Comment;
import me.tiary.domain.Profile;
import me.tiary.domain.Til;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("[Comment] update")
class UpdateTest {
    @Test
    @DisplayName("[Success] comment is updated")
    void successIfCommentIsUpdated() {
        // Given
        final Profile profile = ProfileFactory.createDefaultProfile();

        final Til til = TilFactory.createDefaultTil(profile);

        final Comment comment = CommentFactory.createDefaultComment(profile, til);

        final String content = "New content";

        // When
        comment.update(content);

        // Then
        assertThat(comment.getContent()).isEqualTo(content);
    }
}