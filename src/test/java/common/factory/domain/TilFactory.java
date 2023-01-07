package common.factory.domain;

import common.config.factory.FactoryPreset;
import me.tiary.domain.Profile;
import me.tiary.domain.Til;

public class TilFactory {
    public static Til createDefaultTil(final Profile profile) {
        return create(profile, FactoryPreset.TITLE, FactoryPreset.CONTENT);
    }

    public static Til create(final Profile profile, final String title, final String content) {
        final Til til = Til.builder()
                .profile(profile)
                .title(title)
                .content(content)
                .build();

        til.createUuid();

        return til;
    }
}