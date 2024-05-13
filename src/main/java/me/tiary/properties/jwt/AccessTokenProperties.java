package me.tiary.properties.jwt;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConfigurationProperties(prefix = "jwt.access-token")
@ConstructorBinding
public class AccessTokenProperties extends FiniteJwtProperties {
    public static final String COOKIE_NAME = "access_token";

    public AccessTokenProperties(final String secretKey, final int validSeconds) {
        super(secretKey, validSeconds);
    }

    @RequiredArgsConstructor
    @Getter
    public enum AccessTokenClaim {
        PROFILE_UUID("uuid");

        private final String claim;
    }
}