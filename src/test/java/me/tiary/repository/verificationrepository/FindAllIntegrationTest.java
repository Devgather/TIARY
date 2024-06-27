package me.tiary.repository.verificationrepository;

import common.annotation.repository.RepositoryIntegrationTest;
import common.factory.domain.VerificationFactory;
import common.utility.JpaUtility;
import me.tiary.domain.Verification;
import me.tiary.repository.VerificationRepository;
import me.tiary.utility.common.StringUtility;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RepositoryIntegrationTest
@DisplayName("[VerificationRepository - Integration] findAll")
class FindAllIntegrationTest {
    @Autowired
    private VerificationRepository verificationRepository;

    @PersistenceContext
    private EntityManager em;

    @Test
    @DisplayName("[Success] verifications do not exist")
    void successIfVerificationsDoNotExist() {
        // When
        final List<Verification> result = verificationRepository.findAll();

        // Then
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("[Success] verifications do exist")
    void successIfVerificationsDoExist() {
        // Given
        for (int index = 0; index < 10; index++) {
            final Verification verification = VerificationFactory.create(
                    index + "@example.com",
                    StringUtility.generateRandomString(Verification.CODE_MAX_LENGTH),
                    index % 2 == 0
            );

            verificationRepository.save(verification);
        }

        JpaUtility.flushAndClear(em);

        // When
        final List<Verification> result = verificationRepository.findAll();

        // Then
        assertThat(result).hasSize(10);
    }
}