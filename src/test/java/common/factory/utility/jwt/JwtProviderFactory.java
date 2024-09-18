package common.factory.utility.jwt;

import me.tiary.properties.jwt.AccessTokenProperties;
import me.tiary.properties.jwt.FiniteJwtProperties;
import me.tiary.properties.jwt.JwtProperties;
import me.tiary.properties.jwt.RefreshTokenProperties;
import me.tiary.utility.jwt.FiniteJwtProvider;
import me.tiary.utility.jwt.JwtProvider;

public final class JwtProviderFactory {
    public static JwtProvider createAccessTokenProvider() {
        return create(new AccessTokenProperties("jwt-access-token-secret-key", 300));
    }

    public static JwtProvider createRefreshTokenProvider() {
        return create(new RefreshTokenProperties("jwt-refresh-token-secret-key", 604800));
    }

    public static JwtProvider create(final JwtProperties properties) {
        return new JwtProvider(properties);
    }

    public static FiniteJwtProvider create(final FiniteJwtProperties properties) {
        return new FiniteJwtProvider(properties);
    }
}