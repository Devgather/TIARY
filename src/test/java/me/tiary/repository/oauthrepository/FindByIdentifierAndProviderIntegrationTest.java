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

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RepositoryIntegrationTest
@DisplayName("[OAuthRepository - Integration] findByIdentifierAndProvider")
class FindByIdentifierAndProviderIntegrationTest {
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
    @DisplayName("[Success] identifier and provider do not exist")
    void successIfIdentifierAndProviderDoNotExist() {
        // When
        final Optional<OAuth> result = oAuthRepository.findByIdentifierAndProvider(
                StringUtility.generateRandomString(255), FactoryPreset.OAUTH_PROVIDER
        );

        // Then
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("[Success] identifier and provider do exist")
    void successIfIdentifierAndProviderDoExist() {
        // Given
        final OAuth oAuth = oAuthRepository.save(OAuthFactory.createDefaultOAuth(profile));

        JpaUtility.flushAndClear(em);

        // When
        final Optional<OAuth> result = oAuthRepository.findByIdentifierAndProvider(
                oAuth.getIdentifier(), FactoryPreset.OAUTH_PROVIDER
        );

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().getProfile()).isEqualTo(oAuth.getProfile());
        assertThat(result.get().getIdentifier()).isEqualTo(oAuth.getIdentifier());
        assertThat(result.get().getProvider()).isEqualTo(oAuth.getProvider());
    }
}