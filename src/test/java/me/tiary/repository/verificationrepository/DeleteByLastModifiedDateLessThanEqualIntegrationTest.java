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
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@RepositoryIntegrationTest
@DisplayName("[VerificationRepository - Integration] deleteByLastModifiedDateLessThanEqual")
class DeleteByLastModifiedDateLessThanEqualIntegrationTest {
    @Autowired
    private VerificationRepository verificationRepository;

    @PersistenceContext
    private EntityManager em;

    @Test
    @DisplayName("[Success] verifications are deleted")
    void successIfVerificationsAreDeleted() {
        // Given
        for (int ascii = '0'; ascii <= '9'; ascii++) {
            char character = (char) ascii;

            verificationRepository.save(
                    VerificationFactory.create(
                            character + "@example.com",
                            StringUtility.generateRandomString(Verification.CODE_MAX_LENGTH),
                            false
                    )
            );
        }

        JpaUtility.flushAndClear(em);

        // When
        verificationRepository.deleteByLastModifiedDateLessThanEqual(LocalDateTime.now());

        // Then
        assertThat(verificationRepository.findAll()).isEmpty();
    }
}