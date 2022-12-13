package me.tiary.repository.verificationrepository;

import annotation.repository.RepositoryIntegrationTest;
import config.factory.FactoryPreset;
import factory.domain.VerificationFactory;
import me.tiary.domain.Verification;
import me.tiary.repository.VerificationRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import utility.JpaUtility;
import utility.StringUtility;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@RepositoryIntegrationTest
@DisplayName("[VerificationRepository - Integration] save")
class SaveIntegrationTest {
    @Autowired
    private VerificationRepository verificationRepository;

    @PersistenceContext
    private EntityManager em;

    @Test
    @DisplayName("[Fail] email is null")
    void failIfEmailIsNull() {
        // Given
        final Verification verification = VerificationFactory.create(
                null,
                StringUtility.generateRandomString(Verification.CODE_MAX_LENGTH),
                false
        );

        // When, Then
        assertThrows(DataIntegrityViolationException.class, () -> verificationRepository.save(verification));
    }

    @Test
    @DisplayName("[Fail] email is duplicated")
    void failIfEmailIsDuplicated() {
        // Given
        final Verification verification1 = VerificationFactory.createVerifiedVerification();

        final Verification verification2 = VerificationFactory.createUnverifiedVerification();

        verificationRepository.save(verification1);

        JpaUtility.flushAndClear(em);

        // When, Then
        assertThrows(DataIntegrityViolationException.class, () -> verificationRepository.save(verification2));
    }

    @Test
    @DisplayName("[Fail] code is null")
    void failIfCodeIsNull() {
        // Given
        final Verification verification = VerificationFactory.create(
                FactoryPreset.EMAIL,
                null,
                false
        );

        // When, Then
        assertThrows(DataIntegrityViolationException.class, () -> verificationRepository.save(verification));
    }

    @Test
    @DisplayName("[Fail] code exceeds max length")
    void failIfCodeExceedsMaxLength() {
        // Given
        final Verification verification = VerificationFactory.create(
                FactoryPreset.EMAIL,
                StringUtility.generateRandomString(Verification.CODE_MAX_LENGTH + 1),
                false
        );

        // When, Then
        assertThrows(DataIntegrityViolationException.class, () -> verificationRepository.save(verification));
    }

    @Test
    @DisplayName("[Fail] state is null")
    void failIfStateIsNull() {
        // Given
        final Verification verification = VerificationFactory.create(
                FactoryPreset.EMAIL,
                StringUtility.generateRandomString(Verification.CODE_MAX_LENGTH),
                null
        );

        // When, Then
        assertThrows(DataIntegrityViolationException.class, () -> verificationRepository.save(verification));
    }

    @Test
    @DisplayName("[Success] verification is acceptable")
    void successIfVerificationIsAcceptable() {
        // Given
        final Verification verification = VerificationFactory.createUnverifiedVerification();

        // When
        final Verification result = verificationRepository.save(verification);

        // Then
        assertThat(result.getId()).isNotNull();
        assertThat(result.getUuid().length()).isEqualTo(36);
        assertThat(result.getEmail()).isEqualTo(verification.getEmail());
        assertThat(result.getCode()).isEqualTo(verification.getCode());
        assertThat(result.getState()).isEqualTo(verification.getState());
    }
}