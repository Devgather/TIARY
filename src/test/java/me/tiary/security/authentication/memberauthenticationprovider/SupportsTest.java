package me.tiary.security.authentication.memberauthenticationprovider;

import me.tiary.properties.jwt.AccessTokenProperties;
import me.tiary.properties.jwt.JwtProperties;
import me.tiary.security.authentication.MemberAuthenticationProvider;
import me.tiary.security.userdetails.MemberDetailsService;
import me.tiary.utility.jwt.JwtProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("[MemberAuthenticationProvider] supports")
class SupportsTest {
    private MemberAuthenticationProvider memberAuthenticationProvider;

    @BeforeEach
    void beforeEach() {
        final JwtProperties properties = new AccessTokenProperties("Test", 300);
        final JwtProvider accessTokenProvider = new JwtProvider(properties);
        final AuthenticationUserDetailsService<PreAuthenticatedAuthenticationToken> memberDetailsService = new MemberDetailsService(accessTokenProvider);

        memberAuthenticationProvider = new MemberAuthenticationProvider(memberDetailsService);
    }

    @Test
    @DisplayName("[Success] authentication is not assignable to pre-authenticated authentication token")
    void successIfAuthenticationIsNotAssignableToPreAuthenticatedAuthenticationToken() {
        // Given
        final Class<?> authentication = UsernamePasswordAuthenticationToken.class;

        // When
        final boolean result = memberAuthenticationProvider.supports(authentication);

        // Then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("[Success] authentication is assignable to pre-authenticated authentication token")
    void successIfAuthenticationIsAssignableToPreAuthenticatedAuthenticationToken() {
        // Given
        final Class<?> authentication = PreAuthenticatedAuthenticationToken.class;

        // When
        final boolean result = memberAuthenticationProvider.supports(authentication);

        // Then
        assertThat(result).isTrue();
    }
}