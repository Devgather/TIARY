package common.factory.domain;

import common.config.factory.FactoryPreset;
import me.tiary.domain.OAuth;
import me.tiary.domain.Profile;
import me.tiary.utility.common.StringUtility;

public final class OAuthFactory {
    public static OAuth createDefaultOAuth(final Profile profile) {
        return create(profile, StringUtility.generateRandomString(255), FactoryPreset.OAUTH_PROVIDER);
    }

    public static OAuth create(final Profile profile, final String identifier, final String provider) {
        final OAuth oAuth = OAuth.builder()
                .profile(profile)
                .identifier(identifier)
                .provider(provider)
                .build();

        oAuth.createUuid();

        return oAuth;
    }
}