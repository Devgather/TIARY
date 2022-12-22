package me.tiary.repository.verificationrepository;

import annotation.repository.RepositoryIntegrationTest;
import config.factory.FactoryPreset;
import factory.domain.VerificationFactory;
import me.tiary.domain.Verification;
import me.tiary.repository.VerificationRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import utility.JpaUtility;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@RepositoryIntegrationTest
@DisplayName("[VerificationRepository - Integration] findByUuidAndEmail")
class FindByUuidAndEmailIntegrationTest {
    @Autowired
    private VerificationRepository verificationRepository;

    @PersistenceContext
    private EntityManager em;

    @Test
    @DisplayName("[Success] uuid and email do not exist")
    void successIfUuidAndEmailDoNotExist() {
        // When
        final Optional<Verification> result = verificationRepository.findByUuidAndEmail(UUID.randomUUID().toString(), FactoryPreset.EMAIL);

        // Then
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("[Success] uuid and email do exist")
    void successIfUuidAndEmailDoExist() {
        // Given
        final Verification verification = verificationRepository.save(VerificationFactory.createVerifiedVerification());

        JpaUtility.flushAndClear(em);

        // When
        final Optional<Verification> result = verificationRepository.findByUuidAndEmail(verification.getUuid(), FactoryPreset.EMAIL);

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().getEmail()).isEqualTo(verification.getEmail());
        assertThat(result.get().getCode()).isEqualTo(verification.getCode());
        assertThat(result.get().getState()).isEqualTo(verification.getState());
    }
}