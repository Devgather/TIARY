package me.tiary.repository.profilerepository;

import annotation.repository.RepositoryIntegrationTest;
import me.tiary.domain.Account;
import me.tiary.domain.Profile;
import me.tiary.repository.AccountRepository;
import me.tiary.repository.ProfileRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import utility.JpaUtility;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RepositoryIntegrationTest
@DisplayName("[ProfileRepository - Integration] findByUuidLeftJoinFetchAccount")
class FindByUuidLeftJoinFetchAccountIntegrationTest {
    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private AccountRepository accountRepository;

    @PersistenceContext
    private EntityManager em;

    @Test
    @DisplayName("[Success] uuid does not exist")
    void successIfUuidDoesNotExist() {
        // When
        final Optional<Profile> result = profileRepository.findByUuidLeftJoinFetchAccount("45b2e8bc-df75-4c56-8148-ee03643c11b5");

        // Then
        assertThat(result.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("[Success] uuid does exist and account is null")
    void successIfUuidDoesExistAndAccountIsNull() {
        // Given
        final Profile profile = profileRepository.save(
                Profile.builder()
                        .nickname("Test")
                        .picture("https://example.com/")
                        .build()
        );

        JpaUtility.flushAndClear(em);

        // When
        final Optional<Profile> result = profileRepository.findByUuidLeftJoinFetchAccount(profile.getUuid());

        // Then
        assertThat(result.isPresent()).isTrue();
        assertThat(result.get().getNickname()).isEqualTo(profile.getNickname());
        assertThat(result.get().getPicture()).isEqualTo(profile.getPicture());
        assertThat(result.get().getAccount()).isNull();
    }

    @Test
    @DisplayName("[Success] uuid does exist and account is not null")
    void successIfUuidDoesExistAndAccountIsNotNull() {
        // Given
        final Profile profile = profileRepository.save(
                Profile.builder()
                        .nickname("Test")
                        .picture("https://example.com/")
                        .build()
        );

        accountRepository.save(
                Account.builder()
                        .profile(profile)
                        .email("test@example.com")
                        .password("test")
                        .build()
        );

        JpaUtility.flushAndClear(em);

        // When
        final Optional<Profile> result = profileRepository.findByUuidLeftJoinFetchAccount(profile.getUuid());

        // Then
        assertThat(result.isPresent()).isTrue();
        assertThat(result.get().getNickname()).isEqualTo(profile.getNickname());
        assertThat(result.get().getPicture()).isEqualTo(profile.getPicture());
        assertThat(result.get().getAccount()).isNotNull();
    }
}