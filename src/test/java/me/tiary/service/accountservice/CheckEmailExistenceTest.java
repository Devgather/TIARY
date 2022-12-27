package me.tiary.service.accountservice;

import annotation.service.ServiceTest;
import config.factory.FactoryPreset;
import factory.domain.AccountFactory;
import factory.domain.ProfileFactory;
import me.tiary.domain.Account;
import me.tiary.repository.AccountRepository;
import me.tiary.service.AccountService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;

@ServiceTest
@DisplayName("[AccountService] checkEmailExistence")
class CheckEmailExistenceTest {
    @InjectMocks
    private AccountService accountService;

    @Mock
    private AccountRepository accountRepository;

    @Test
    @DisplayName("[Success] email does not exist")
    void successIfEmailDoesNotExist() {
        // Given
        doReturn(Optional.empty())
                .when(accountRepository)
                .findByEmail(any(String.class));

        // When
        final boolean result = accountService.checkEmailExistence(FactoryPreset.EMAIL);

        // Then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("[Success] email does exist")
    void successIfEmailDoesExist() {
        // Given
        final Account account = AccountFactory.createDefaultAccount(ProfileFactory.createDefaultProfile());

        doReturn(Optional.ofNullable(account))
                .when(accountRepository)
                .findByEmail(eq(account.getEmail()));

        // When
        final boolean result = accountService.checkEmailExistence(FactoryPreset.EMAIL);

        // Then
        assertThat(result).isTrue();
    }
}