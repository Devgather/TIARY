package me.tiary.service.accountservice;

import me.tiary.domain.Account;
import me.tiary.repository.AccountRepository;
import me.tiary.service.AccountService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
@DisplayName("[AccountService] checkEmailDuplication")
class CheckEmailDuplicationTest {
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
        final boolean result = accountService.checkEmailDuplication("test@example.com");

        // Then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("[Success] email does exist")
    void successIfEmailDoesExist() {
        // Given
        doReturn(Optional.ofNullable(Account.builder().build()))
                .when(accountRepository)
                .findByEmail(eq("test@example.com"));

        // When
        final boolean result = accountService.checkEmailDuplication("test@example.com");

        // Then
        assertThat(result).isTrue();
    }
}