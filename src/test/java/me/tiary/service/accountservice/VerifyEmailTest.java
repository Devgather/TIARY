package me.tiary.service.accountservice;

import common.annotation.service.ServiceTest;
import common.config.factory.FactoryPreset;
import common.factory.domain.VerificationFactory;
import common.factory.dto.account.AccountVerificationRequestDtoFactory;
import me.tiary.domain.Verification;
import me.tiary.dto.account.AccountVerificationRequestDto;
import me.tiary.dto.account.AccountVerificationResponseDto;
import me.tiary.exception.AccountException;
import me.tiary.exception.status.AccountStatus;
import me.tiary.repository.VerificationRepository;
import me.tiary.service.AccountService;
import me.tiary.utility.common.StringUtility;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;

@ServiceTest
@DisplayName("[AccountService] verifyEmail")
class VerifyEmailTest {
    @InjectMocks
    private AccountService accountService;

    @Mock
    private VerificationRepository verificationRepository;

    @Spy
    private ModelMapper modelMapper;

    @BeforeEach
    void beforeEach() {
        modelMapper.getConfiguration()
                .setFieldMatchingEnabled(true)
                .setFieldAccessLevel(Configuration.AccessLevel.PRIVATE);
    }

    @Test
    @DisplayName("[Fail] email verification is not requested")
    void failEmailVerificationIsNotRequested() {
        // Given
        doReturn(Optional.empty())
                .when(verificationRepository)
                .findByEmail(any(String.class));

        final AccountVerificationRequestDto requestDto = AccountVerificationRequestDtoFactory.createDefaultAccountVerificationRequestDto(
                StringUtility.generateRandomString(Verification.CODE_MAX_LENGTH)
        );

        // When, Then
        final AccountException result = assertThrows(AccountException.class, () -> accountService.verifyEmail(requestDto));

        assertThat(result.getStatus()).isEqualTo(AccountStatus.UNREQUESTED_EMAIL_VERIFICATION);
    }

    @Test
    @DisplayName("[Fail] email is not unverified")
    void failIfEmailIsNotUnverified() {
        // Given
        final Verification verifiedVerification = VerificationFactory.createVerifiedVerification();

        doReturn(Optional.ofNullable(verifiedVerification))
                .when(verificationRepository)
                .findByEmail(eq(verifiedVerification.getEmail()));

        final AccountVerificationRequestDto requestDto = AccountVerificationRequestDtoFactory.createDefaultAccountVerificationRequestDto(
                verifiedVerification.getCode()
        );

        // When, Then
        final AccountException result = assertThrows(AccountException.class, () -> accountService.verifyEmail(requestDto));

        assertThat(result.getStatus()).isEqualTo(AccountStatus.VERIFIED_EMAIL);
    }

    @Test
    @DisplayName("[Fail] code is incorrect")
    void failIfCodeIsIncorrect() {
        // Given
        final Verification unverifiedVerification = VerificationFactory.create(FactoryPreset.EMAIL, "000000", false);

        doReturn(Optional.ofNullable(unverifiedVerification))
                .when(verificationRepository)
                .findByEmail(eq(unverifiedVerification.getEmail()));

        final AccountVerificationRequestDto requestDto = AccountVerificationRequestDtoFactory.createDefaultAccountVerificationRequestDto(
                "123456"
        );

        // When, Then
        final AccountException result = assertThrows(AccountException.class, () -> accountService.verifyEmail(requestDto));

        assertThat(result.getStatus()).isEqualTo(AccountStatus.INCORRECT_CODE);
    }

    @Test
    @DisplayName("[Success] email is verified")
    void successIfEmailIsVerified() {
        // Given
        final Verification unverifiedVerification = VerificationFactory.createUnverifiedVerification();

        doReturn(Optional.ofNullable(unverifiedVerification))
                .when(verificationRepository)
                .findByEmail(eq(unverifiedVerification.getEmail()));

        final AccountVerificationRequestDto requestDto = AccountVerificationRequestDtoFactory.createDefaultAccountVerificationRequestDto(
                unverifiedVerification.getCode()
        );

        // When
        final AccountVerificationResponseDto result = accountService.verifyEmail(requestDto);

        // Then
        assertThat(result.getUuid()).isEqualTo(unverifiedVerification.getUuid());
    }
}