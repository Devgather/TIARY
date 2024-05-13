package me.tiary.properties.jwt;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConfigurationProperties(prefix = "jwt.refresh-token")
@ConstructorBinding
public class RefreshTokenProperties extends FiniteJwtProperties {
    public static final String COOKIE_NAME = "refresh_token";

    public RefreshTokenProperties(final String secretKey, final int validSeconds) {
        super(secretKey, validSeconds);
    }

    @RequiredArgsConstructor
    @Getter
    public enum RefreshTokenClaim {
        PROFILE_UUID("uuid");

        private final String claim;
    }
}