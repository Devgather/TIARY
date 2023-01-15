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

@DisplayName("[Comment] updateContent")
class UpdateContentTest {
    @Test
    @DisplayName("[Success] content is updated")
    void successIfContentIsUpdated() {
        // Given
        final Profile profile = ProfileFactory.createDefaultProfile();

        final Til til = TilFactory.createDefaultTil(profile);

        final Comment comment = CommentFactory.createDefaultComment(profile, til);

        final String content = "New";

        // When
        comment.updateContent(content);

        // Then
        assertThat(comment.getContent()).isEqualTo(content);
    }
}
