package common.factory.domain;

import common.config.factory.FactoryPreset;
import me.tiary.domain.Tag;

public class TagFactory {
    public static Tag createDefaultTag() {
        return create(FactoryPreset.TAG);
    }

    public static Tag create(final String name) {
        final Tag tag = Tag.builder()
                .name(name)
                .build();

        tag.createUuid();

        return tag;
    }
}