package me.tiary.domain.verification;

import common.factory.domain.VerificationFactory;
import me.tiary.domain.Verification;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("[Verification] refreshCode")
class RefreshCodeTest {
    @Test
    @DisplayName("[Fail] verification is verified")
    void failIfVerificationIsVerified() {
        // Given
        final Verification verifiedVerification = VerificationFactory.createVerifiedVerification();

        // When, Then
        assertThrows(IllegalStateException.class, verifiedVerification::refreshCode);
    }

    @Test
    @DisplayName("[Success] code is refreshed")
    void successIfCodeIsRefreshed() {
        // Given
        final Verification unverifiedVerification = VerificationFactory.createUnverifiedVerification();
        final String oldCode = unverifiedVerification.getCode();

        // When
        unverifiedVerification.refreshCode();

        // Then
        assertThat(unverifiedVerification.getCode()).isNotEqualTo(oldCode);
    }
}