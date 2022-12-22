package me.tiary.utility.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import me.tiary.properties.jwt.JwtProperties;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

public class JwtProvider {
    private final Algorithm algorithm;

    private final int validSeconds;

    public JwtProvider(final JwtProperties properties) {
        this.algorithm = Algorithm.HMAC256(properties.getSecretKey());
        this.validSeconds = properties.getValidSeconds();
    }

    public String generate(final Map<String, ?> payload) throws JWTCreationException {
        final Calendar calendar = Calendar.getInstance();

        calendar.setTime(new Date());
        calendar.add(Calendar.SECOND, validSeconds);

        return JWT.create()
                .withExpiresAt(calendar.getTime())
                .withPayload(payload)
                .sign(algorithm);
    }

    public DecodedJWT verify(final String token) throws JWTVerificationException {
        final JWTVerifier verifier = JWT.require(algorithm).build();

        return verifier.verify(token);
    }
}