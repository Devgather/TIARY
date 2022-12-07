package me.tiary.repository.accountrepository;

import annotation.repository.RepositoryIntegrationTest;
import me.tiary.domain.Account;
import me.tiary.domain.Profile;
import me.tiary.repository.AccountRepository;
import me.tiary.repository.ProfileRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@RepositoryIntegrationTest
@DisplayName("[AccountRepository] save")
class SaveTest {
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ProfileRepository profileRepository;

    private Profile profile;

    @BeforeEach
    void beforeEach() {
        final Profile profile = Profile.builder()
                .nickname("Test")
                .picture("https://example.com/")
                .build();

        this.profile = profileRepository.save(profile);
    }

    @Test
    @DisplayName("[Fail] profile is null")
    void failIfProfileIsNull() {
        // Given
        final Account account = Account.builder()
                .profile(null)
                .email("test@example.com")
                .password("test")
                .build();

        // Then
        assertThrows(DataIntegrityViolationException.class, () -> accountRepository.save(account));
    }

    @Test
    @DisplayName("[Fail] email is null")
    void failIfEmailIsNull() {
        // Given
        final Account account = Account.builder()
                .profile(profile)
                .email(null)
                .password("test")
                .build();

        // Then
        assertThrows(DataIntegrityViolationException.class, () -> accountRepository.save(account));
    }

    @Test
    @DisplayName("[Fail] email is duplicated")
    void failIfEmailIsDuplicated() {
        // Given
        final Profile profile1 = profileRepository.save(
                Profile.builder()
                        .nickname("Test1")
                        .picture("https://example.com/")
                        .build()
        );

        final Profile profile2 = profileRepository.save(
                Profile.builder()
                        .nickname("Test2")
                        .picture("https://example.com/")
                        .build()
        );

        final Account account1 = Account.builder()
                .profile(profile1)
                .email("test@example.com")
                .password("test1")
                .build();

        final Account account2 = Account.builder()
                .profile(profile2)
                .email("test@example.com")
                .password("test2")
                .build();

        accountRepository.save(account1);

        // Then
        assertThrows(DataIntegrityViolationException.class, () -> accountRepository.save(account2));
    }

    @Test
    @DisplayName("[Fail] password is null")
    void failIfPasswordIsNull() {
        // Given
        final Account account = Account.builder()
                .profile(profile)
                .email("test@example.com")
                .password(null)
                .build();

        // Then
        assertThrows(DataIntegrityViolationException.class, () -> accountRepository.save(account));
    }

    @Test
    @DisplayName("[Success] account is acceptable")
    void successIfAccountIsAcceptable() {
        // Given
        final Account account = Account.builder()
                .profile(profile)
                .email("test@example.com")
                .password("test")
                .build();

        // When
        final Account result = accountRepository.save(account);

        // Then
        assertThat(result.getProfile()).isEqualTo(account.getProfile());
        assertThat(result.getUuid().length()).isEqualTo(36);
        assertThat(result.getEmail()).isEqualTo(account.getEmail());
        assertThat(result.getPassword()).isEqualTo(account.getPassword());
    }
}