package me.tiary.security.userdetails.memberdetailsservice;

import me.tiary.properties.jwt.AccessTokenProperties;
import me.tiary.properties.jwt.JwtProperties;
import me.tiary.security.userdetails.MemberDetails;
import me.tiary.security.userdetails.MemberDetailsService;
import me.tiary.utility.jwt.JwtProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("[MemberDetailsService] loadUserDetails")
class LoadUserDetailsTest {
    private MemberDetailsService memberDetailsService;

    @BeforeEach
    void beforeEach() {
        final JwtProperties properties = new AccessTokenProperties("Test", 300);
        final JwtProvider accessTokenProvider = new JwtProvider(properties);

        memberDetailsService = new MemberDetailsService(accessTokenProvider);
    }

    @Test
    @DisplayName("[Fail] access token is invalid")
    void failIfAccessTokenIsInvalid() {
        // Given
        final String accessToken = "a.b.c";

        final PreAuthenticatedAuthenticationToken token = new PreAuthenticatedAuthenticationToken(null, accessToken);

        // Then
        assertThrows(BadCredentialsException.class, () -> memberDetailsService.loadUserDetails(token));
    }

    @Test
    @DisplayName("[Fail] access token algorithm is mismatch")
    void failIfAccessTokenAlgorithmIsMismatch() {
        // Given
        // Algorithm = HMAC512, Payload = { "data": "Test" }, Secret Key = Test
        final String accessToken = "eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJkYXRhIjoiVGVzdCJ9.ehkf3FQVbKY4XFGiOdTHcL8rYmmzss8Q-3iSctozmefcAbzibfos-Ch_lydD9FKTN_LmJIVj4YunKi3VmnInUw";

        final PreAuthenticatedAuthenticationToken token = new PreAuthenticatedAuthenticationToken(null, accessToken);

        // Then
        assertThrows(BadCredentialsException.class, () -> memberDetailsService.loadUserDetails(token));
    }

    @Test
    @DisplayName("[Fail] access token has expired")
    void failIfAccessTokenHasExpired() {
        // Given
        // Algorithm = HMAC256, Payload = { "data": "Test", "exp": 0 }, Secret Key = Test
        final String accessToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJkYXRhIjoiVGVzdCIsImV4cCI6MH0.ZbhLANDWkjWbQwkoKPDv_Xi8kfObrtTE8Ow_6Hk5D1A";

        final PreAuthenticatedAuthenticationToken token = new PreAuthenticatedAuthenticationToken(null, accessToken);

        // Then
        assertThrows(BadCredentialsException.class, () -> memberDetailsService.loadUserDetails(token));
    }

    @Test
    @DisplayName("[Fail] access token signature is invalid")
    void failIfAccessTokenSignatureIsInvalid() {
        // Given
        // Algorithm = HMAC256, Payload = { "data": "Test" }, Secret Key = Invalid Secret Key
        final String accessToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJkYXRhIjoiVGVzdCJ9.8m2ipdRrtI-MVw6MS8IRff-uMG-mH70maH0tR-gPAW8";

        final PreAuthenticatedAuthenticationToken token = new PreAuthenticatedAuthenticationToken(null, accessToken);

        // Then
        assertThrows(BadCredentialsException.class, () -> memberDetailsService.loadUserDetails(token));
    }

    @Test
    @DisplayName("[Success] access token is valid")
    void successIfAccessTokenIsValid() {
        // Given
        // Algorithm = HMAC256, Payload = { "uuid": "cbf0f220-97b8-4312-82ce-f98266c428d4" }, Secret Key = Test
        final String accessToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1dWlkIjoiY2JmMGYyMjAtOTdiOC00MzEyLTgyY2UtZjk4MjY2YzQyOGQ0In0.G0z3gVEh_uwH0cq0stN6JE7PkwC8L4DwzginXwX-1qg";

        final PreAuthenticatedAuthenticationToken token = new PreAuthenticatedAuthenticationToken(null, accessToken);

        // When
        final MemberDetails memberDetails = (MemberDetails) memberDetailsService.loadUserDetails(token);

        // Then
        assertThat(memberDetails.getProfileUuid()).isEqualTo("cbf0f220-97b8-4312-82ce-f98266c428d4");
        assertThat(memberDetails.getAuthorities()).isNull();
    }
}