package me.tiary.security.authentication.memberauthenticationprovider;

import me.tiary.properties.jwt.AccessTokenProperties;
import me.tiary.properties.jwt.JwtProperties;
import me.tiary.security.authentication.MemberAuthenticationProvider;
import me.tiary.security.userdetails.MemberDetailsService;
import me.tiary.utility.jwt.JwtProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("[MemberAuthenticationProvider] authenticate")
class AuthenticateTest {
    private MemberAuthenticationProvider memberAuthenticationProvider;

    @BeforeEach
    void beforeEach() {
        final JwtProperties properties = new AccessTokenProperties("Test", 300);
        final JwtProvider accessTokenProvider = new JwtProvider(properties);
        final AuthenticationUserDetailsService<PreAuthenticatedAuthenticationToken> memberDetailsService = new MemberDetailsService(accessTokenProvider);

        memberAuthenticationProvider = new MemberAuthenticationProvider(memberDetailsService);
    }

    @Test
    @DisplayName("[Fail] authentication is not supported")
    void failIfAuthenticationIsNotSupported() {
        // Given
        final Authentication authentication = new UsernamePasswordAuthenticationToken(null, null);

        // When
        final Authentication result = memberAuthenticationProvider.authenticate(authentication);

        // Then
        assertThat(result).isNull();
    }

    @Test
    @DisplayName("[Fail] credentials is null")
    void failIfCredentialsIsNull() {
        // Given
        final Authentication authentication = new PreAuthenticatedAuthenticationToken(null, null);

        // When, Then
        assertThrows(BadCredentialsException.class, () -> memberAuthenticationProvider.authenticate(authentication));
    }

    @Test
    @DisplayName("[Success] authentication is acceptable")
    void successIfAuthenticationIsAcceptable() {
        // Given
        // Algorithm = HMAC256, Payload = { "uuid": "cbf0f220-97b8-4312-82ce-f98266c428d4" }, Secret Key = Test
        final String accessToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1dWlkIjoiY2JmMGYyMjAtOTdiOC00MzEyLTgyY2UtZjk4MjY2YzQyOGQ0In0.G0z3gVEh_uwH0cq0stN6JE7PkwC8L4DwzginXwX-1qg";
        final Authentication authentication = new PreAuthenticatedAuthenticationToken(null, accessToken);

        // When
        final Authentication result = memberAuthenticationProvider.authenticate(authentication);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getPrincipal()).isInstanceOf(UserDetails.class);
        assertThat(result.getCredentials()).isEqualTo(authentication.getCredentials());
        assertThat(result.getAuthorities()).isNotNull();
        assertThat(result.getDetails()).isEqualTo(authentication.getDetails());
    }
}