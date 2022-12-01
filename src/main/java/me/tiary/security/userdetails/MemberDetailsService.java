package me.tiary.security.userdetails;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.tiary.properties.jwt.AccessTokenProperties.AccessTokenClaim;
import me.tiary.utility.jwt.JwtProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

@RequiredArgsConstructor
@Slf4j
public class MemberDetailsService implements AuthenticationUserDetailsService<PreAuthenticatedAuthenticationToken> {
    private final JwtProvider accessTokenProvider;

    @Override
    public UserDetails loadUserDetails(final PreAuthenticatedAuthenticationToken token) throws AuthenticationException {
        try {
            final String accessToken = (String) token.getCredentials();
            final DecodedJWT decodedAccessToken = accessTokenProvider.verify(accessToken);

            final String profileUuid = decodedAccessToken.getClaim(AccessTokenClaim.PROFILE_UUID.getClaim()).asString();

            log.info("Member authentication request: {}", profileUuid);

            return MemberDetails.builder()
                    .profileUuid(profileUuid)
                    .build();
        } catch (JWTVerificationException ex) {
            throw new BadCredentialsException(ex.getMessage());
        }
    }
}