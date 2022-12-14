package me.tiary.domain.verification;

import factory.domain.VerificationFactory;
import me.tiary.domain.Verification;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("[Verification] verify")
class VerifyTest {
    @Test
    @DisplayName("[Success] unverified state is changed to verified state")
    void successIfUnverifiedStateIsChangedToVerifiedState() {
        // Given
        final Verification unverifiedVerification = VerificationFactory.createUnverifiedVerification();

        // When
        unverifiedVerification.verify();

        // Then
        assertThat(unverifiedVerification.getState()).isTrue();
    }

    @Test
    @DisplayName("[Success] verified state is not changed")
    void successIfVerifiedStateIsNotChanged() {
        // Given
        final Verification verifiedVerification = VerificationFactory.createVerifiedVerification();

        // When
        verifiedVerification.verify();

        // Then
        assertThat(verifiedVerification.getState()).isTrue();
    }
}