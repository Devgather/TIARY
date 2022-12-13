package me.tiary.service.accountservice;

import annotation.service.ServiceTest;
import config.factory.FactoryPreset;
import factory.domain.VerificationFactory;
import me.tiary.domain.Verification;
import me.tiary.exception.AccountException;
import me.tiary.exception.status.AccountStatus;
import me.tiary.repository.VerificationRepository;
import me.tiary.service.AccountService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;

@ServiceTest
@DisplayName("[AccountService] sendVerificationMail")
class SendVerificationMailTest {
    @InjectMocks
    private AccountService accountService;

    @Mock
    private VerificationRepository verificationRepository;

    @Test
    @DisplayName("[Fail] email is verified")
    void failIfEmailIsVerified() {
        // Given
        final Verification verification = VerificationFactory.createVerifiedVerification();

        doReturn(Optional.ofNullable(verification))
                .when(verificationRepository)
                .findByEmail(eq(verification.getEmail()));

        // When, Then
        final AccountException result = assertThrows(AccountException.class, () -> accountService.sendVerificationMail(FactoryPreset.EMAIL));

        assertThat(result.getStatus()).isEqualTo(AccountStatus.VERIFIED_EMAIL);
    }
}