package me.tiary.repository.verificationrepository;

import annotation.repository.RepositoryIntegrationTest;
import me.tiary.domain.Verification;
import me.tiary.repository.VerificationRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import utility.StringUtility;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RepositoryIntegrationTest
@DisplayName("[VerificationRepository] findByEmail")
class FindByEmailTest {
    @Autowired
    private VerificationRepository verificationRepository;

    @Test
    @DisplayName("[Success] email does not exist")
    void successIfEmailDoesNotExist() {
        // When
        final Optional<Verification> result = verificationRepository.findByEmail("test@example.com");

        // Then
        assertThat(result.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("[Success] email does exist")
    void successIfEmailDoesExist() {
        // Given
        final Verification verification = Verification.builder()
                .email("test@example.com")
                .code(StringUtility.generateRandomString(Verification.CODE_MAX_LENGTH))
                .state(false)
                .build();

        verificationRepository.save(verification);

        // When
        final Optional<Verification> result = verificationRepository.findByEmail("test@example.com");

        // Then
        assertThat(result.isPresent()).isTrue();
        assertThat(result.get().getUuid().length()).isEqualTo(36);
        assertThat(result.get().getEmail()).isEqualTo(verification.getEmail());
        assertThat(result.get().getCode()).isEqualTo(verification.getCode());
        assertThat(result.get().getState()).isEqualTo(verification.getState());
    }
}