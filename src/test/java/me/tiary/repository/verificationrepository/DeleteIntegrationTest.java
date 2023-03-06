package me.tiary.repository.verificationrepository;

import common.annotation.repository.RepositoryIntegrationTest;
import common.factory.domain.VerificationFactory;
import common.utility.JpaUtility;
import me.tiary.domain.Verification;
import me.tiary.repository.VerificationRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.InvalidDataAccessApiUsageException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@RepositoryIntegrationTest
@DisplayName("[VerificationRepository - Integration] delete")
class DeleteIntegrationTest {
    @Autowired
    private VerificationRepository verificationRepository;

    @PersistenceContext
    private EntityManager em;

    @Test
    @DisplayName("[Fail] verification is null")
    void failIfVerificationIsNull() {
        // When, Then
        assertThrows(InvalidDataAccessApiUsageException.class, () -> verificationRepository.delete(null));
    }

    @Test
    @DisplayName("[Success] verification is deleted")
    void successIfVerificationIsDeleted() {
        // Given
        final Verification verification = verificationRepository.save(VerificationFactory.createVerifiedVerification());

        JpaUtility.flushAndClear(em);

        // When
        verificationRepository.delete(verification);

        final Optional<Verification> result = verificationRepository.findById(verification.getId());

        // Then
        assertThat(result).isNotPresent();
    }
}