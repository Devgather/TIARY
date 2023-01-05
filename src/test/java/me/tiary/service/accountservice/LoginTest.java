package me.tiary.service.accountservice;

import common.factory.domain.AccountFactory;
import common.factory.domain.ProfileFactory;
import common.factory.dto.account.AccountLoginRequestDtoFactory;
import common.factory.utility.jwt.JwtProviderFactory;
import me.tiary.domain.Account;
import me.tiary.dto.account.AccountLoginRequestDto;
import me.tiary.dto.account.AccountLoginResponseDto;
import me.tiary.exception.AccountException;
import me.tiary.exception.status.AccountStatus;
import me.tiary.properties.jwt.AccessTokenProperties;
import me.tiary.properties.jwt.JwtProperties;
import me.tiary.properties.jwt.RefreshTokenProperties;
import me.tiary.repository.AccountRepository;
import me.tiary.service.AccountService;
import me.tiary.utility.jwt.JwtProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
@DisplayName("[AccountService] login")
public class LoginTest {
    private AccountService accountService;

    @Mock
    private AccountRepository accountRepository;

    @Spy
    private ModelMapper modelMapper;

    @Spy
    private BCryptPasswordEncoder passwordEncoder;

    private JwtProvider accessTokenProvider;

    private JwtProvider refreshTokenProvider;

    @BeforeEach
    void beforeEach() {
        modelMapper.getConfiguration()
                .setFieldMatchingEnabled(true)
                .setFieldAccessLevel(Configuration.AccessLevel.PRIVATE);

        final JwtProperties accessTokenProperties = new AccessTokenProperties("jwt-access-token-secret-key", 300);
        final JwtProperties refreshTokenProperties = new RefreshTokenProperties("jwt-refresh-token-secret-key", 604800);

        accessTokenProvider = new JwtProvider(accessTokenProperties);
        refreshTokenProvider = new JwtProvider(refreshTokenProperties);

        accountService = new AccountService(
                accountRepository,
                null,
                null,
                null,
                modelMapper,
                passwordEncoder,
                null,
                accessTokenProvider,
                refreshTokenProvider
        );
    }

    @Test
    @DisplayName("[Fail] email does not exist")
    void failIfEmailDoesNotExist() {
        // Given
        doReturn(Optional.empty())
                .when(accountRepository)
                .findByEmailJoinFetchProfile(any(String.class));

        final AccountLoginRequestDto requestDto = AccountLoginRequestDtoFactory.createDefaultAccountLoginRequestDto();

        // When, Then
        final AccountException result = assertThrows(AccountException.class, () -> accountService.login(requestDto));

        assertThat(result.getStatus()).isEqualTo(AccountStatus.NOT_EXISTING_EMAIL);
    }

    @Test
    @DisplayName("[Fail] password does not match")
    void failIfPasswordDoesNotMatch() {
        // Given
        final Account account = AccountFactory.createDefaultAccount(ProfileFactory.createDefaultProfile());

        doReturn(Optional.ofNullable(account))
                .when(accountRepository)
                .findByEmailJoinFetchProfile(eq(account.getEmail()));

        final AccountLoginRequestDto requestDto = AccountLoginRequestDtoFactory.create("test@example.com", "wrong password");

        // When, Then
        final AccountException result = assertThrows(AccountException.class, () -> accountService.login(requestDto));

        assertThat(result.getStatus()).isEqualTo(AccountStatus.NOT_MATCHING_PASSWORD);
    }

    @Test
    @DisplayName("[Success] credentials are acceptable")
    void successIfCredentialsAreAcceptable() {
        // Given
        final Account account = AccountFactory.createDefaultAccount(ProfileFactory.createDefaultProfile());

        final JwtProvider refreshTokenProvider = JwtProviderFactory.createRefreshTokenProvider();

        doReturn(Optional.ofNullable(account))
                .when(accountRepository)
                .findByEmailJoinFetchProfile(eq(account.getEmail()));

        final AccountLoginRequestDto requestDto = AccountLoginRequestDtoFactory.createDefaultAccountLoginRequestDto();

        // When
        final AccountLoginResponseDto result = accountService.login(requestDto);

        // Then
        assertDoesNotThrow(() -> accessTokenProvider.verify(result.getAccessToken()));
        assertDoesNotThrow(() -> refreshTokenProvider.verify(result.getRefreshToken()));
        assertThat(result.getRefreshTokenValidSeconds()).isEqualTo(refreshTokenProvider.getValidSeconds());

    }
}