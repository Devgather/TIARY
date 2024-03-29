package me.tiary.repository.verificationrepository;

import common.annotation.repository.RepositoryIntegrationTest;
import common.config.factory.FactoryPreset;
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
@DisplayName("[VerificationRepository - Integration] findByEmail")
class FindByEmailIntegrationTest {
    @Autowired
    private VerificationRepository verificationRepository;

    @PersistenceContext
    private EntityManager em;

    @Test
    @DisplayName("[Success] email does not exist")
    void successIfEmailDoesNotExist() {
        // When
        final Optional<Verification> result = verificationRepository.findByEmail(FactoryPreset.EMAIL);

        // Then
        assertThat(result).isNotPresent();
    }

    @Test
    @DisplayName("[Success] email does exist")
    void successIfEmailDoesExist() {
        // Given
        final Verification verification = VerificationFactory.createVerifiedVerification();

        verificationRepository.save(verification);

        JpaUtility.flushAndClear(em);

        // When
        final Optional<Verification> result = verificationRepository.findByEmail(FactoryPreset.EMAIL);

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().getEmail()).isEqualTo(verification.getEmail());
        assertThat(result.get().getCode()).isEqualTo(verification.getCode());
        assertThat(result.get().getState()).isEqualTo(verification.getState());
    }
}