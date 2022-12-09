package me.tiary.repository.accountrepository;

import annotation.repository.RepositoryIntegrationTest;
import config.factory.FactoryPreset;
import factory.domain.AccountFactory;
import factory.domain.ProfileFactory;
import me.tiary.domain.Account;
import me.tiary.domain.Profile;
import me.tiary.repository.AccountRepository;
import me.tiary.repository.ProfileRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import utility.JpaUtility;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@RepositoryIntegrationTest
@DisplayName("[AccountRepository - Integration] save")
class SaveIntegrationTest {
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
    @DisplayName("[Fail] profile is null")
    void failIfProfileIsNull() {
        // Given
        final Account account = AccountFactory.create(null, FactoryPreset.EMAIL, FactoryPreset.PASSWORD);

        // When, Then
        assertThrows(DataIntegrityViolationException.class, () -> accountRepository.save(account));
    }

    @Test
    @DisplayName("[Fail] email is null")
    void failIfEmailIsNull() {
        // Given
        final Account account = AccountFactory.create(profile, null, FactoryPreset.PASSWORD);

        // When, Then
        assertThrows(DataIntegrityViolationException.class, () -> accountRepository.save(account));
    }

    @Test
    @DisplayName("[Fail] email is duplicated")
    void failIfEmailIsDuplicated() {
        // Given
        final Profile profile1 = profileRepository.save(ProfileFactory.create("Test1", FactoryPreset.PICTURE));

        final Profile profile2 = profileRepository.save(ProfileFactory.create("Test2", FactoryPreset.PICTURE));

        final Account account1 = AccountFactory.createDefaultAccount(profile1);

        accountRepository.save(account1);

        JpaUtility.flushAndClear(em);

        final Account account2 = AccountFactory.createDefaultAccount(profile2);

        // When, Then
        assertThrows(DataIntegrityViolationException.class, () -> accountRepository.save(account2));
    }

    @Test
    @DisplayName("[Fail] password is null")
    void failIfPasswordIsNull() {
        // Given
        final Account account = AccountFactory.create(profile, FactoryPreset.EMAIL, null);

        // When, Then
        assertThrows(DataIntegrityViolationException.class, () -> accountRepository.save(account));
    }

    @Test
    @DisplayName("[Success] account is acceptable")
    void successIfAccountIsAcceptable() {
        // Given
        final Account account = AccountFactory.createDefaultAccount(profile);

        // When
        final Account result = accountRepository.save(account);

        // Then
        assertThat(result.getId()).isNotNull();
        assertThat(result.getProfile()).isEqualTo(account.getProfile());
        assertThat(result.getUuid().length()).isEqualTo(36);
        assertThat(result.getEmail()).isEqualTo(account.getEmail());
        assertThat(result.getPassword()).isEqualTo(account.getPassword());
    }
}