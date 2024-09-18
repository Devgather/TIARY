package me.tiary.repository.verificationrepository;

import common.annotation.repository.RepositoryIntegrationTest;
import common.factory.domain.VerificationFactory;
import common.utility.JpaUtility;
import me.tiary.domain.Verification;
import me.tiary.repository.VerificationRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RepositoryIntegrationTest
@DisplayName("[VerificationRepository - Integration] findById")
class FindByIdIntegrationTest {
    @Autowired
    private VerificationRepository verificationRepository;

    @PersistenceContext
    private EntityManager em;

    @Test
    @DisplayName("[Success] id does not exist")
    void successIfIdDoesNotExist() {
        // When
        final Optional<Verification> result = verificationRepository.findById(0L);

        // Then
        assertThat(result).isNotPresent();
    }

    @Test
    @DisplayName("[Success] id does exist")
    void successIfIdDoesExist() {
        // Given
        final Verification verification = VerificationFactory.createVerifiedVerification();

        verificationRepository.save(verification);

        JpaUtility.flushAndClear(em);

        // When
        final Optional<Verification> result = verificationRepository.findById(verification.getId());

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().getEmail()).isEqualTo(verification.getEmail());
        assertThat(result.get().getCode()).isEqualTo(verification.getCode());
        assertThat(result.get().getState()).isEqualTo(verification.getState());
    }
}