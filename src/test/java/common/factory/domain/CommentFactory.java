package common.factory.domain;

import common.config.factory.FactoryPreset;
import me.tiary.domain.Comment;
import me.tiary.domain.Profile;
import me.tiary.domain.Til;

public final class CommentFactory {
    public static Comment createDefaultComment(final Profile profile, final Til til) {
        return create(profile, til, FactoryPreset.CONTENT);
    }

    public static Comment create(final Profile profile, final Til til, final String content) {
        final Comment comment = Comment.builder()
                .profile(profile)
                .til(til)
                .content(content)
                .build();

        comment.createUuid();

        return comment;
    }
}