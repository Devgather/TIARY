package common.factory.domain;

import me.tiary.domain.Tag;
import me.tiary.domain.Til;
import me.tiary.domain.TilTag;

public final class TilTagFactory {
    public static TilTag create(final Til til, final Tag tag) {
        final TilTag tilTag = TilTag.builder()
                .til(til)
                .tag(tag)
                .build();

        tilTag.createId();
        tilTag.createUuid();

        return tilTag;
    }
}