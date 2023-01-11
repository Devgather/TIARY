package me.tiary.exception.handler.security;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.tiary.exception.handler.ExceptionResponse;
import me.tiary.properties.jwt.AccessTokenProperties;
import me.tiary.properties.jwt.AccessTokenProperties.AccessTokenClaim;
import me.tiary.properties.jwt.RefreshTokenProperties;
import me.tiary.properties.jwt.RefreshTokenProperties.RefreshTokenClaim;
import me.tiary.utility.jwt.JwtProvider;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Slf4j
public class AuthenticationExceptionHandler implements AuthenticationEntryPoint {
    private final JwtProvider accessTokenProvider;

    private final JwtProvider refreshTokenProvider;

    private final ObjectMapper objectMapper;

    @Override
    public void commence(final HttpServletRequest request,
                         final HttpServletResponse response,
                         final AuthenticationException authException) throws IOException {
        try {
            final Cookie accessTokenCookie = WebUtils.getCookie(request, AccessTokenProperties.COOKIE_NAME);
            final String accessToken = (accessTokenCookie == null) ? (null) : (accessTokenCookie.getValue());

            if (checkAccessTokenExpiration(accessToken)) {
                final Cookie refreshTokenCookie = WebUtils.getCookie(request, RefreshTokenProperties.COOKIE_NAME);
                final String refreshToken = (refreshTokenCookie == null) ? (null) : (refreshTokenCookie.getValue());

                final DecodedJWT decodedRefreshToken = verifyRefreshToken(refreshToken);

                String profileUuid = decodedRefreshToken.getClaim(RefreshTokenClaim.PROFILE_UUID.getClaim()).toString();

                profileUuid = profileUuid.substring(1, profileUuid.length() - 1);

                response.addCookie(
                        createAccessTokenCookie(Map.of(AccessTokenClaim.PROFILE_UUID.getClaim(), profileUuid))
                );

                response.addCookie(
                        createRefreshTokenCookie(Map.of(RefreshTokenClaim.PROFILE_UUID.getClaim(), profileUuid))
                );

                response.sendRedirect(request.getRequestURI());
            }
        } catch (final Exception ex) {
            final String[] uriTokens = request.getRequestURI().substring(1).split("/");

            log.warn("Authentication exception occurrence: {}", authException.getMessage());

            response.addCookie(deleteCookie(AccessTokenProperties.COOKIE_NAME));
            response.addCookie(deleteCookie(RefreshTokenProperties.COOKIE_NAME));

            if (uriTokens.length > 0 && uriTokens[0].equals("api")) {
                final String responseBody = objectMapper.writeValueAsString(
                        new ExceptionResponse(List.of(authException.getMessage()))
                );

                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                response.setCharacterEncoding(StandardCharsets.UTF_8.name());
                response.getWriter().write(responseBody);
            } else {
                response.sendRedirect(request.getRequestURI());
            }
        }
    }

    private boolean checkAccessTokenExpiration(final String accessToken) {
        if (accessToken == null) {
            throw new IllegalArgumentException();
        }

        try {
            accessTokenProvider.verify(accessToken);

            return false;
        } catch (final TokenExpiredException tokenExpiredException) {
            return true;
        } catch (final JWTVerificationException jwtVerificationException) {
            throw new BadCredentialsException(jwtVerificationException.getMessage());
        }
    }

    private DecodedJWT verifyRefreshToken(final String refreshToken) {
        if (refreshToken == null) {
            throw new IllegalArgumentException();
        }

        try {
            return refreshTokenProvider.verify(refreshToken);
        } catch (final JWTVerificationException ex) {
            throw new BadCredentialsException(ex.getMessage());
        }
    }

    private Cookie createAccessTokenCookie(final Map<String, ?> payload) {
        final String accessToken = accessTokenProvider.generate(payload);

        final Cookie accessTokenCookie = new Cookie(AccessTokenProperties.COOKIE_NAME, accessToken);

        accessTokenCookie.setHttpOnly(true);
        accessTokenCookie.setSecure(true);
        accessTokenCookie.setPath("/");

        return accessTokenCookie;
    }

    private Cookie createRefreshTokenCookie(final Map<String, ?> payload) {
        final String refreshToken = refreshTokenProvider.generate(payload);

        final Cookie refreshTokenCookie = new Cookie(RefreshTokenProperties.COOKIE_NAME, refreshToken);

        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setSecure(true);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setMaxAge(refreshTokenProvider.getValidSeconds());

        return refreshTokenCookie;
    }

    private Cookie deleteCookie(final String name) {
        final Cookie cookie = new Cookie(name, null);

        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);

        return cookie;
    }
}