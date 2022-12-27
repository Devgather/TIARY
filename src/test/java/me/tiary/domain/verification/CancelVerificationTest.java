package me.tiary.domain.verification;

import factory.domain.VerificationFactory;
import me.tiary.domain.Verification;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("[Verification] cancelVerification")
class CancelVerificationTest {
    @Test
    @DisplayName("[Success] unverified state is not changed")
    void successIfUnverifiedStateIsNotChanged() {
        // Given
        final Verification unverifiedVerification = VerificationFactory.createUnverifiedVerification();

        // When
        unverifiedVerification.cancelVerification();

        // Then
        assertThat(unverifiedVerification.getState()).isFalse();
    }

    @Test
    @DisplayName("[Success] verified state is changed to unverified state")
    void successIfVerifiedStateIsChangedToUnverifiedState() {
        // Given
        final Verification verifiedVerification = VerificationFactory.createVerifiedVerification();

        // When
        verifiedVerification.cancelVerification();

        // Then
        assertThat(verifiedVerification.getState()).isFalse();
    }
}