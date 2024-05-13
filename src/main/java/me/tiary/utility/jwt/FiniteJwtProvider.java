package me.tiary.utility.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTCreationException;
import me.tiary.properties.jwt.FiniteJwtProperties;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

public class FiniteJwtProvider extends JwtProvider {
    private final int validSeconds;

    public FiniteJwtProvider(final FiniteJwtProperties properties) {
        super(properties);

        this.validSeconds = properties.getValidSeconds();
    }

    @Override
    public String generate(final Map<String, ?> payload) throws JWTCreationException {
        final Calendar calendar = Calendar.getInstance();

        calendar.setTime(new Date());
        calendar.add(Calendar.SECOND, validSeconds);

        return JWT.create()
                .withExpiresAt(calendar.getTime())
                .withPayload(payload)
                .sign(algorithm);
    }
}