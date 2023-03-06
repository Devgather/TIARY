package me.tiary.service.accountservice;

import common.annotation.service.ServiceTest;
import common.factory.domain.AccountFactory;
import common.factory.domain.ProfileFactory;
import common.factory.domain.VerificationFactory;
import common.factory.dto.account.AccountCreationRequestDtoFactory;
import me.tiary.domain.Account;
import me.tiary.domain.Profile;
import me.tiary.domain.Verification;
import me.tiary.dto.account.AccountCreationRequestDto;
import me.tiary.dto.account.AccountCreationResponseDto;
import me.tiary.exception.AccountException;
import me.tiary.exception.status.AccountStatus;
import me.tiary.repository.AccountRepository;
import me.tiary.repository.ProfileRepository;
import me.tiary.repository.VerificationRepository;
import me.tiary.service.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

@ServiceTest
@DisplayName("[AccountService] register")
class RegisterTest {
    @InjectMocks
    private AccountService accountService;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private ProfileRepository profileRepository;

    @Mock
    private VerificationRepository verificationRepository;

    @Spy
    private ModelMapper modelMapper;

    @Spy
    private BCryptPasswordEncoder passwordEncoder;

    @BeforeEach
    void beforeEach() {
        modelMapper.getConfiguration()
                .setFieldMatchingEnabled(true)
                .setFieldAccessLevel(Configuration.AccessLevel.PRIVATE);
    }

    @Test
    @DisplayName("[Fail] email does exist")
    void failIfEmailDoesExist() {
        // Given
        final Account account = AccountFactory.createDefaultAccount(ProfileFactory.createDefaultProfile());

        doReturn(Optional.of(account))
                .when(accountRepository)
                .findByEmail(account.getEmail());

        final AccountCreationRequestDto requestDto = AccountCreationRequestDtoFactory.createDefaultAccountCreationRequestDto(
                UUID.randomUUID().toString(), UUID.randomUUID().toString()
        );

        // When, Then
        final AccountException result = assertThrows(AccountException.class, () -> accountService.register(requestDto));

        assertThat(result.getStatus()).isEqualTo(AccountStatus.EXISTING_EMAIL);
    }

    @Test
    @DisplayName("[Fail] email verification is not requested")
    void failEmailVerificationIsNotRequested() {
        // Given
        doReturn(Optional.empty())
                .when(accountRepository)
                .findByEmail(any(String.class));

        doReturn(Optional.empty())
                .when(verificationRepository)
                .findByUuidAndEmail(any(String.class), any(String.class));

        final AccountCreationRequestDto requestDto = AccountCreationRequestDtoFactory.createDefaultAccountCreationRequestDto(
                UUID.randomUUID().toString(), UUID.randomUUID().toString()
        );

        // When, Then
        final AccountException result = assertThrows(AccountException.class, () -> accountService.register(requestDto));

        assertThat(result.getStatus()).isEqualTo(AccountStatus.UNREQUESTED_EMAIL_VERIFICATION);
    }

    @Test
    @DisplayName("[Fail] email is not verified")
    void failIfEmailIsNotVerified() {
        // Given
        doReturn(Optional.empty())
                .when(accountRepository)
                .findByEmail(any(String.class));

        final Verification unverifiedVerification = VerificationFactory.createUnverifiedVerification();

        doReturn(Optional.of(unverifiedVerification))
                .when(verificationRepository)
                .findByUuidAndEmail(unverifiedVerification.getUuid(), unverifiedVerification.getEmail());

        final AccountCreationRequestDto requestDto = AccountCreationRequestDtoFactory.createDefaultAccountCreationRequestDto(
                unverifiedVerification.getUuid(), UUID.randomUUID().toString()
        );

        // When, Then
        final AccountException result = assertThrows(AccountException.class, () -> accountService.register(requestDto));

        assertThat(result.getStatus()).isEqualTo(AccountStatus.UNVERIFIED_EMAIL);
    }

    @Test
    @DisplayName("[Fail] profile uuid does not exist")
    void failProfileUuidDoesNotExist() {
        // Given
        doReturn(Optional.empty())
                .when(accountRepository)
                .findByEmail(any(String.class));

        final Verification verifiedVerification = VerificationFactory.createVerifiedVerification();

        doReturn(Optional.of(verifiedVerification))
                .when(verificationRepository)
                .findByUuidAndEmail(verifiedVerification.getUuid(), verifiedVerification.getEmail());

        doReturn(Optional.empty())
                .when(profileRepository)
                .findByUuidLeftJoinFetchAccount(any(String.class));

        final AccountCreationRequestDto requestDto = AccountCreationRequestDtoFactory.createDefaultAccountCreationRequestDto(
                verifiedVerification.getUuid(), UUID.randomUUID().toString()
        );

        // When, Then
        final AccountException result = assertThrows(AccountException.class, () -> accountService.register(requestDto));

        assertThat(result.getStatus()).isEqualTo(AccountStatus.NOT_EXISTING_PROFILE_UUID);
    }

    @Test
    @DisplayName("[Fail] profile already has account")
    void failIfProfileAlreadyHasAccount() {
        // Given
        doReturn(Optional.empty())
                .when(accountRepository)
                .findByEmail(any(String.class));

        final Verification verifiedVerification = VerificationFactory.createVerifiedVerification();

        doReturn(Optional.of(verifiedVerification))
                .when(verificationRepository)
                .findByUuidAndEmail(verifiedVerification.getUuid(), verifiedVerification.getEmail());

        final Profile profile = ProfileFactory.createDefaultProfile();

        AccountFactory.createDefaultAccount(profile);

        doReturn(Optional.of(profile))
                .when(profileRepository)
                .findByUuidLeftJoinFetchAccount(profile.getUuid());

        final AccountCreationRequestDto requestDto = AccountCreationRequestDtoFactory.createDefaultAccountCreationRequestDto(
                verifiedVerification.getUuid(), profile.getUuid()
        );

        // When, Then
        final AccountException result = assertThrows(AccountException.class, () -> accountService.register(requestDto));

        assertThat(result.getStatus()).isEqualTo(AccountStatus.EXISTING_ANOTHER_ACCOUNT_ON_PROFILE);
    }

    @Test
    @DisplayName("[Success] account is creatable")
    void successIfAccountIsCreatable() {
        // Given
        doReturn(Optional.empty())
                .when(accountRepository)
                .findByEmail(any(String.class));

        final Verification verifiedVerification = VerificationFactory.createVerifiedVerification();

        doReturn(Optional.of(verifiedVerification))
                .when(verificationRepository)
                .findByUuidAndEmail(verifiedVerification.getUuid(), verifiedVerification.getEmail());

        final Profile profile = ProfileFactory.createDefaultProfile();

        doReturn(Optional.of(profile))
                .when(profileRepository)
                .findByUuidLeftJoinFetchAccount(profile.getUuid());

        doReturn(AccountFactory.createDefaultAccount(ProfileFactory.createDefaultProfile()))
                .when(accountRepository)
                .save(any(Account.class));

        final AccountCreationRequestDto requestDto = AccountCreationRequestDtoFactory.createDefaultAccountCreationRequestDto(
                verifiedVerification.getUuid(), profile.getUuid()
        );

        // When
        final AccountCreationResponseDto result = accountService.register(requestDto);

        // Then
        assertThat(result.getEmail()).isEqualTo(requestDto.getEmail());
    }
}