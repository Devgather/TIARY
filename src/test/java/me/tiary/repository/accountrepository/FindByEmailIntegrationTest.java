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
import utility.JpaUtility;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RepositoryIntegrationTest
@DisplayName("[AccountRepository - Integration] findByEmail")
class FindByEmailIntegrationTest {
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ProfileRepository profileRepository;

    @PersistenceContext
    private EntityManager em;

    private Profile profile;

    @BeforeEach
    void beforeEach() {
        final Profile profile = Profile.builder()
                .nickname("Test")
                .picture("https://example.com/")
                .build();

        this.profile = profileRepository.save(profile);

        JpaUtility.flushAndClear(em);
    }

    @Test
    @DisplayName("[Success] email does not exist")
    void successIfEmailDoesNotExist() {
        // When
        final Optional<Account> result = accountRepository.findByEmail("test@example.com");

        // Then
        assertThat(result.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("[Success] email does exist")
    void successIfEmailDoesExist() {
        // Given
        final Account account = Account.builder()
                .profile(profile)
                .email("test@example.com")
                .password("test")
                .build();

        accountRepository.save(account);

        JpaUtility.flushAndClear(em);

        // When
        final Optional<Account> result = accountRepository.findByEmail("test@example.com");

        // Then
        assertThat(result.isPresent()).isTrue();
        assertThat(result.get().getProfile()).isEqualTo(account.getProfile());
        assertThat(result.get().getEmail()).isEqualTo(account.getEmail());
        assertThat(result.get().getPassword()).isEqualTo(account.getPassword());
    }
}