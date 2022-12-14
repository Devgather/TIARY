package common.factory.utility.jwt;

import me.tiary.properties.jwt.AccessTokenProperties;
import me.tiary.properties.jwt.JwtProperties;
import me.tiary.properties.jwt.RefreshTokenProperties;
import me.tiary.utility.jwt.JwtProvider;

public final class JwtProviderFactory {
    public static JwtProvider createAccessTokenProvider() {
        final JwtProperties properties = new AccessTokenProperties("jwt-access-token-secret-key", 300);

        return create(properties);
    }

    public static JwtProvider createRefreshTokenProvider() {
        final JwtProperties properties = new RefreshTokenProperties("jwt-refresh-token-secret-key", 604800);

        return create(properties);
    }

    public static JwtProvider create(final JwtProperties properties) {
        return new JwtProvider(properties);
    }
}