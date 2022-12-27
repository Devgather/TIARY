package me.tiary.service.accountservice;

import common.annotation.service.ServiceTest;
import common.config.factory.FactoryPreset;
import common.factory.domain.AccountFactory;
import common.factory.domain.ProfileFactory;
import common.factory.domain.VerificationFactory;
import me.tiary.domain.Account;
import me.tiary.domain.Verification;
import me.tiary.exception.AccountException;
import me.tiary.exception.status.AccountStatus;
import me.tiary.repository.AccountRepository;
import me.tiary.repository.VerificationRepository;
import me.tiary.service.AccountService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.mail.javamail.JavaMailSender;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Optional;
import java.util.Properties;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ServiceTest
@DisplayName("[AccountService] sendVerificationMail")
class SendVerificationMailTest {
    @InjectMocks
    private AccountService accountService;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private VerificationRepository verificationRepository;

    @Spy
    private SpringTemplateEngine templateEngine;

    @Mock
    private JavaMailSender mailSender;

    @Test
    @DisplayName("[Fail] email does exist")
    void failIfEmailDoesExist() {
        // Given
        final Account account = AccountFactory.createDefaultAccount(ProfileFactory.createDefaultProfile());

        doReturn(Optional.ofNullable(account))
                .when(accountRepository)
                .findByEmail(eq(account.getEmail()));

        // When
        final AccountException result = assertThrows(AccountException.class, () -> accountService.sendVerificationMail(FactoryPreset.EMAIL));

        // Then
        assertThat(result.getStatus()).isEqualTo(AccountStatus.EXISTING_EMAIL);
    }

    @Test
    @DisplayName("[Success] verification does not exist")
    void successIfVerificationDoesNotExist() throws MessagingException {
        // Given
        doReturn(Optional.empty())
                .when(accountRepository)
                .findByEmail(any(String.class));

        doReturn(Optional.empty())
                .when(verificationRepository)
                .findByEmail(any(String.class));

        doReturn(VerificationFactory.createUnverifiedVerification())
                .when(verificationRepository)
                .save(any(Verification.class));

        final Session session = Session.getInstance(new Properties());
        final InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        final MimeMessage mimeMessage = new MimeMessage(session, inputStream);

        doReturn(mimeMessage)
                .when(mailSender)
                .createMimeMessage();

        // When, Then
        assertDoesNotThrow(() -> accountService.sendVerificationMail(FactoryPreset.EMAIL));
        verify(verificationRepository, times(1)).save(any(Verification.class));
    }

    @Test
    @DisplayName("[Success] verification does exist and is unverified")
    void successIfVerificationDoesExistAndIsUnverified() throws MessagingException {
        // Given
        doReturn(Optional.empty())
                .when(accountRepository)
                .findByEmail(any(String.class));

        final Verification unverifiedVerification = VerificationFactory.createUnverifiedVerification();

        doReturn(Optional.ofNullable(unverifiedVerification))
                .when(verificationRepository)
                .findByEmail(eq(unverifiedVerification.getEmail()));

        final Session session = Session.getInstance(new Properties());
        final InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        final MimeMessage mimeMessage = new MimeMessage(session, inputStream);

        doReturn(mimeMessage)
                .when(mailSender)
                .createMimeMessage();

        // When, Then
        assertDoesNotThrow(() -> accountService.sendVerificationMail(FactoryPreset.EMAIL));
        verify(verificationRepository, times(0)).save(any(Verification.class));
    }

    @Test
    @DisplayName("[Success] verification does exist and is verified")
    void successIfVerificationDoesExistAndIsVerified() throws MessagingException {
        // Given
        doReturn(Optional.empty())
                .when(accountRepository)
                .findByEmail(any(String.class));

        final Verification verifiedVerification = VerificationFactory.createVerifiedVerification();

        doReturn(Optional.ofNullable(verifiedVerification))
                .when(verificationRepository)
                .findByEmail(eq(verifiedVerification.getEmail()));

        final Session session = Session.getInstance(new Properties());
        final InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        final MimeMessage mimeMessage = new MimeMessage(session, inputStream);

        doReturn(mimeMessage)
                .when(mailSender)
                .createMimeMessage();

        // When, Then
        assertDoesNotThrow(() -> accountService.sendVerificationMail(FactoryPreset.EMAIL));
        verify(verificationRepository, times(0)).save(any(Verification.class));
    }
}