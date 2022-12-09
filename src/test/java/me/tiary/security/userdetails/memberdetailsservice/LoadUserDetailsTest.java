package me.tiary.security.userdetails.memberdetailsservice;

import factory.utility.jwt.JwtProviderFactory;
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
        final JwtProvider accessTokenProvider = JwtProviderFactory.createAccessTokenProvider();

        memberDetailsService = new MemberDetailsService(accessTokenProvider);
    }

    @Test
    @DisplayName("[Fail] access token is invalid")
    void failIfAccessTokenIsInvalid() {
        // Given
        final String accessToken = "a.b.c";

        final PreAuthenticatedAuthenticationToken token = new PreAuthenticatedAuthenticationToken(null, accessToken);

        // When, Then
        assertThrows(BadCredentialsException.class, () -> memberDetailsService.loadUserDetails(token));
    }

    @Test
    @DisplayName("[Fail] access token algorithm is mismatch")
    void failIfAccessTokenAlgorithmIsMismatch() {
        // Given
        // Algorithm = HMAC512, Payload = { "uuid": "cbf0f220-97b8-4312-82ce-f98266c428d4" }, Secret Key = jwt-access-token-secret-key
        final String accessToken = "eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJ1dWlkIjoiY2JmMGYyMjAtOTdiOC00MzEyLTgyY2UtZjk4MjY2YzQyOGQ0In0.cCJL6etYw6r7tlXpuJqEQ7LccTOSsKbBW3LzavvqSvLPSJRBt8w7yMAB6d53Bs_FXf7YRqF5F9xrWyKMr7_KZw";

        final PreAuthenticatedAuthenticationToken token = new PreAuthenticatedAuthenticationToken(null, accessToken);

        // When, Then
        assertThrows(BadCredentialsException.class, () -> memberDetailsService.loadUserDetails(token));
    }

    @Test
    @DisplayName("[Fail] access token has expired")
    void failIfAccessTokenHasExpired() {
        // Given
        // Algorithm = HMAC256, Payload = { "uuid": "cbf0f220-97b8-4312-82ce-f98266c428d4", "exp": 0 }, Secret Key = jwt-access-token-secret-key
        final String accessToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1dWlkIjoiY2JmMGYyMjAtOTdiOC00MzEyLTgyY2UtZjk4MjY2YzQyOGQ0IiwiZXhwIjowfQ.3xL9qBUNtPop2bKoKloECcG0Pu-nCJC7tdE3tTXJ2fk";

        final PreAuthenticatedAuthenticationToken token = new PreAuthenticatedAuthenticationToken(null, accessToken);

        // When, Then
        assertThrows(BadCredentialsException.class, () -> memberDetailsService.loadUserDetails(token));
    }

    @Test
    @DisplayName("[Fail] access token signature is invalid")
    void failIfAccessTokenSignatureIsInvalid() {
        // Given
        // Algorithm = HMAC256, Payload = { "uuid": "cbf0f220-97b8-4312-82ce-f98266c428d4" }, Secret Key = invalid-secret-key
        final String accessToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1dWlkIjoiY2JmMGYyMjAtOTdiOC00MzEyLTgyY2UtZjk4MjY2YzQyOGQ0In0.ftqXO7VbB5rpAaJks-B9V2a43TmE23TOtTbVzzxAwg4";

        final PreAuthenticatedAuthenticationToken token = new PreAuthenticatedAuthenticationToken(null, accessToken);

        // When, Then
        assertThrows(BadCredentialsException.class, () -> memberDetailsService.loadUserDetails(token));
    }

    @Test
    @DisplayName("[Success] access token is valid")
    void successIfAccessTokenIsValid() {
        // Given
        // Algorithm = HMAC256, Payload = { "uuid": "cbf0f220-97b8-4312-82ce-f98266c428d4" }, Secret Key = jwt-access-token-secret-key
        final String accessToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1dWlkIjoiY2JmMGYyMjAtOTdiOC00MzEyLTgyY2UtZjk4MjY2YzQyOGQ0In0.rftGC07wvthl89A-lHN4NzeP2gcVv9UxTTnST3Nhqz8";

        final PreAuthenticatedAuthenticationToken token = new PreAuthenticatedAuthenticationToken(null, accessToken);

        // When
        final MemberDetails memberDetails = (MemberDetails) memberDetailsService.loadUserDetails(token);

        // Then
        assertThat(memberDetails.getProfileUuid()).isEqualTo("cbf0f220-97b8-4312-82ce-f98266c428d4");
        assertThat(memberDetails.getAuthorities()).isNull();
    }
}