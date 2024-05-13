package me.tiary.properties.jwt;

import lombok.Getter;

@Getter
public abstract class FiniteJwtProperties extends JwtProperties {
    private final int validSeconds;

    protected FiniteJwtProperties(final String secretKey, final int validSeconds) {
        super(secretKey);

        this.validSeconds = validSeconds;
    }
}