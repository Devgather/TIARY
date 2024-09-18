package me.tiary.utility.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import me.tiary.properties.jwt.JwtProperties;

import java.util.Map;

public class JwtProvider {
    protected final Algorithm algorithm;

    public JwtProvider(final JwtProperties properties) {
        this.algorithm = Algorithm.HMAC256(properties.getSecretKey());
    }

    public String generate(final Map<String, ?> payload) throws JWTCreationException {
        return JWT.create()
                .withPayload(payload)
                .sign(algorithm);
    }

    public DecodedJWT verify(final String token) throws JWTVerificationException {
        final JWTVerifier verifier = JWT.require(algorithm).build();

        return verifier.verify(token);
    }
}