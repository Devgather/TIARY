package me.tiary.security.oauth2.authentication;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.tiary.domain.OAuth;
import me.tiary.domain.Profile;
import me.tiary.properties.aws.AwsStorageProperties;
import me.tiary.properties.jwt.AccessTokenProperties;
import me.tiary.properties.jwt.RefreshTokenProperties;
import me.tiary.repository.OAuthRepository;
import me.tiary.repository.ProfileRepository;
import me.tiary.security.oauth2.user.OAuth2Member;
import me.tiary.utility.common.StringUtility;
import me.tiary.utility.jwt.JwtProvider;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    public static final String REDIRECT_URL = "/";

    private final OAuthRepository oAuthRepository;

    private final ProfileRepository profileRepository;

    private final JwtProvider accessTokenProvider;

    private final JwtProvider refreshTokenProvider;

    private final AwsStorageProperties awsStorageProperties;

    @Override
    @Transactional
    public void onAuthenticationSuccess(final HttpServletRequest request,
                                        final HttpServletResponse response,
                                        final Authentication authentication) throws IOException {
        final Cookie accessTokenCookie = WebUtils.getCookie(request, AccessTokenProperties.COOKIE_NAME);
        final String accessToken = (accessTokenCookie == null) ? (null) : (accessTokenCookie.getValue());

        final OAuth2Member member = (OAuth2Member) authentication.getPrincipal();

        final OAuth oAuth = oAuthRepository.findByIdentifierAndProvider(member.getName(), member.getRegistrationId())
                .orElseGet(() -> {
                    final Profile profile = findProfileWithAccessToken(accessToken)
                            .orElseGet(this::createProfile);

                    return createOAuth(profile, member.getName(), member.getRegistrationId());
                });

        final String profileUuid = oAuth.getProfile().getUuid();

        response.addCookie(
                createAccessTokenCookie(Map.of(AccessTokenProperties.AccessTokenClaim.PROFILE_UUID.getClaim(), profileUuid))
        );

        response.addCookie(
                createRefreshTokenCookie(Map.of(RefreshTokenProperties.RefreshTokenClaim.PROFILE_UUID.getClaim(), profileUuid))
        );

        final String provider = Character.toUpperCase(member.getRegistrationId().charAt(0)) + member.getRegistrationId().substring(1);

        log.info("{} OAuth 2.0 authentication request: {}", provider, profileUuid);

        getRedirectStrategy().sendRedirect(request, response, REDIRECT_URL);
    }

    private OAuth createOAuth(final Profile profile, final String identifier, final String provider) {
        try {
            final OAuth oAuth = OAuth.builder()
                    .profile(profile)
                    .identifier(identifier)
                    .provider(provider)
                    .build();

            return oAuthRepository.save(oAuth);
        } catch (final DataIntegrityViolationException ex) {
            throw new IllegalArgumentException(ex.getMessage());
        }
    }

    private Optional<Profile> findProfileWithAccessToken(final String accessToken) {
        if (accessToken == null) {
            return Optional.empty();
        }

        try {
            final DecodedJWT decodedAccessToken = accessTokenProvider.verify(accessToken);

            final String profileUuid = decodedAccessToken.getClaim(AccessTokenProperties.AccessTokenClaim.PROFILE_UUID.getClaim()).asString();

            return profileRepository.findByUuid(profileUuid);
        } catch (final JWTVerificationException ex) {
            throw new BadCredentialsException(ex.getMessage());
        }
    }

    private Profile createProfile() {
        String nickname = StringUtility.generateRandomString(Profile.NICKNAME_MAX_LENGTH);

        while (profileRepository.findByNickname(nickname).isPresent()) {
            nickname = StringUtility.generateRandomString(Profile.NICKNAME_MAX_LENGTH);
        }

        String storageUrl = awsStorageProperties.getUrl();

        if (storageUrl.endsWith("/")) {
            storageUrl = storageUrl.substring(0, storageUrl.length() - 1);
        }

        final Profile profile = Profile.builder()
                .nickname(nickname)
                .picture(storageUrl + Profile.BASIC_PICTURE)
                .build();

        return profileRepository.save(profile);
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
}