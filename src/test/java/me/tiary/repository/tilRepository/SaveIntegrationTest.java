package me.tiary.repository.tilRepository;

import common.annotation.repository.RepositoryIntegrationTest;
import common.config.factory.FactoryPreset;
import common.factory.domain.ProfileFactory;
import common.factory.domain.TilFactory;
import common.utility.JpaUtility;
import me.tiary.domain.Profile;
import me.tiary.domain.Til;
import me.tiary.repository.ProfileRepository;
import me.tiary.repository.TilRepository;
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
@DisplayName("[TilRepository - Integration] save")
class SaveIntegrationTest {
    @Autowired
    private TilRepository tilRepository;

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
        final Til til = TilFactory.create(null, FactoryPreset.TITLE, FactoryPreset.CONTENT);

        // When, Then
        assertThrows(DataIntegrityViolationException.class, () -> tilRepository.save(til));
    }

    @Test
    @DisplayName("[Fail] title is null")
    void failIfTitleIsNull() {
        // Given
        final Til til = TilFactory.create(profile, null, FactoryPreset.CONTENT);

        // When, Then
        assertThrows(DataIntegrityViolationException.class, () -> tilRepository.save(til));
    }

    @Test
    @DisplayName("[Fail] content is null")
    void failIfContentIsNull() {
        // Given
        final Til til = TilFactory.create(profile, FactoryPreset.TITLE, null);

        // When, Then
        assertThrows(DataIntegrityViolationException.class, () -> tilRepository.save(til));
    }

    @Test
    @DisplayName("[Success] til is acceptable")
    void successIfTilIsAcceptable() {
        // Given
        final Til til = TilFactory.createDefaultTil(profile);

        // When
        final Til result = tilRepository.save(til);

        // Then
        assertThat(result.getId()).isNotNull();
        assertThat(result.getProfile()).isEqualTo(til.getProfile());
        assertThat(result.getUuid()).isEqualTo(til.getUuid());
        assertThat(result.getTitle()).isEqualTo(til.getTitle());
        assertThat(result.getContent()).isEqualTo(til.getContent());

    }
}