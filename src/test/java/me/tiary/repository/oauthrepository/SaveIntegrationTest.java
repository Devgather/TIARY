package me.tiary.repository.oauthrepository;

import common.annotation.repository.RepositoryIntegrationTest;
import common.config.factory.FactoryPreset;
import common.factory.domain.OAuthFactory;
import common.factory.domain.ProfileFactory;
import common.utility.JpaUtility;
import me.tiary.domain.OAuth;
import me.tiary.domain.Profile;
import me.tiary.repository.OAuthRepository;
import me.tiary.repository.ProfileRepository;
import me.tiary.utility.common.StringUtility;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@RepositoryIntegrationTest
@DisplayName("[OAuthRepository - Integration] save")
class SaveIntegrationTest {
    @Autowired
    private OAuthRepository oAuthRepository;

    @Autowired
    private ProfileRepository profileRepository;

    @PersistenceContext
    private EntityManager em;

    private Profile profile;

    @BeforeEach
    void beforeEach() {
        profile = profileRepository.save(ProfileFactory.createDefaultProfile());

        JpaUtility.flushAndClear(em);
    }

    @Test
    @DisplayName("[Fail] profile is null")
    void failIfProfileIsNull() {
        // Given
        final OAuth oAuth = OAuthFactory.create(
                null, StringUtility.generateRandomString(255), FactoryPreset.OAUTH_PROVIDER
        );

        // When, Then
        assertThrows(DataIntegrityViolationException.class, () -> oAuthRepository.save(oAuth));
    }

    @Test
    @DisplayName("[Fail] identifier is null")
    void failIfIdentifierIsNull() {
        // Given
        final OAuth oAuth = OAuthFactory.create(
                profile, null, FactoryPreset.OAUTH_PROVIDER
        );

        // When, Then
        assertThrows(DataIntegrityViolationException.class, () -> oAuthRepository.save(oAuth));
    }

    @Test
    @DisplayName("[Fail] identifier is duplicated")
    void failIfIdentifierIsDuplicated() {
        // Given
        final Profile profile1 = profileRepository.save(ProfileFactory.create("Test1", FactoryPreset.STORAGE + FactoryPreset.PICTURE));

        final Profile profile2 = profileRepository.save(ProfileFactory.create("Test2", FactoryPreset.STORAGE + FactoryPreset.PICTURE));

        final String identifier = StringUtility.generateRandomString(255);

        final OAuth oAuth1 = OAuthFactory.create(profile1, identifier, FactoryPreset.OAUTH_PROVIDER);

        oAuthRepository.save(oAuth1);

        JpaUtility.flushAndClear(em);

        final OAuth oAuth2 = OAuthFactory.create(profile2, identifier, FactoryPreset.OAUTH_PROVIDER);

        // When, Then
        assertThrows(DataIntegrityViolationException.class, () -> oAuthRepository.save(oAuth2));
    }

    @Test
    @DisplayName("[Fail] provider is null")
    void failIfProviderIsNull() {
        // Given
        final OAuth oAuth = OAuthFactory.create(
                profile, StringUtility.generateRandomString(255), null
        );

        // When, Then
        assertThrows(DataIntegrityViolationException.class, () -> oAuthRepository.save(oAuth));
    }

    @Test
    @DisplayName("[Success] oauth is acceptable")
    void successIfOAuthIsAcceptable() {
        // Given
        final OAuth oAuth = OAuthFactory.createDefaultOAuth(profile);

        // When
        final OAuth result = oAuthRepository.save(oAuth);

        // Then
        assertThat(result.getId()).isNotNull();
        assertThat(result.getProfile()).isEqualTo(oAuth.getProfile());
        assertThat(result.getUuid().length()).isEqualTo(36);
        assertThat(result.getIdentifier()).isEqualTo(oAuth.getIdentifier());
        assertThat(result.getProvider()).isEqualTo(oAuth.getProvider());
    }
}