package factory.domain;

import config.factory.FactoryPreset;
import me.tiary.domain.Profile;

public final class ProfileFactory {
    public static Profile createDefaultProfile() {
        return create(FactoryPreset.NICKNAME, FactoryPreset.PICTURE);
    }

    public static Profile create(final String nickname, final String picture) {
        final Profile profile = Profile.builder()
                .nickname(nickname)
                .picture(picture)
                .build();

        profile.createUuid();

        return profile;
    }
}