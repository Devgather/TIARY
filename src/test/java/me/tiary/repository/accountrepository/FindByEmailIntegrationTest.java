package me.tiary.repository.accountrepository;

import common.annotation.repository.RepositoryIntegrationTest;
import common.config.factory.FactoryPreset;
import common.factory.domain.AccountFactory;
import common.factory.domain.ProfileFactory;
import common.utility.JpaUtility;
import me.tiary.domain.Account;
import me.tiary.domain.Profile;
import me.tiary.repository.AccountRepository;
import me.tiary.repository.ProfileRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

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
        profile = profileRepository.save(ProfileFactory.createDefaultProfile());

        JpaUtility.flushAndClear(em);
    }

    @Test
    @DisplayName("[Success] email does not exist")
    void successIfEmailDoesNotExist() {
        // When
        final Optional<Account> result = accountRepository.findByEmail(FactoryPreset.EMAIL);

        // Then
        assertThat(result.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("[Success] email does exist")
    void successIfEmailDoesExist() {
        // Given
        final Account account = AccountFactory.createDefaultAccount(profile);

        accountRepository.save(account);

        JpaUtility.flushAndClear(em);

        // When
        final Optional<Account> result = accountRepository.findByEmail(FactoryPreset.EMAIL);

        // Then
        assertThat(result.isPresent()).isTrue();
        assertThat(result.get().getProfile()).isEqualTo(account.getProfile());
        assertThat(result.get().getEmail()).isEqualTo(account.getEmail());
        assertThat(result.get().getPassword()).isEqualTo(account.getPassword());
    }
}