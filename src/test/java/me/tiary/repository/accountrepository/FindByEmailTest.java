package me.tiary.repository.accountrepository;

import me.tiary.domain.Account;
import me.tiary.domain.Profile;
import me.tiary.repository.AccountRepository;
import me.tiary.repository.ProfileRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DisplayName("[AccountRepository] findByEmail")
class FindByEmailTest {
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ProfileRepository profileRepository;

    private Profile profile;

    @BeforeEach
    void beforeEach() {
        final Profile profile = Profile.builder()
                .nickname("Test")
                .build();

        this.profile = profileRepository.save(profile);
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

        // When
        final Optional<Account> result = accountRepository.findByEmail("test@example.com");

        // Then
        assertThat(result.isPresent()).isTrue();
        assertThat(result.get().getProfile()).isEqualTo(profile);
        assertThat(result.get().getEmail()).isEqualTo("test@example.com");
        assertThat(result.get().getPassword()).isEqualTo("test");
    }
}