package me.tiary.repository.tilrepository;

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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@RepositoryIntegrationTest
@DisplayName("[TilRepository - Integration] saveAll")
class SaveAllIntegrationTest {
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
        final List<Til> tils = List.of(TilFactory.create(null, FactoryPreset.TITLE, FactoryPreset.CONTENT));

        // When, Then
        assertThrows(DataIntegrityViolationException.class, () -> tilRepository.saveAll(tils));
    }

    @Test
    @DisplayName("[Fail] title is null")
    void failIfTitleIsNull() {
        // Given
        final List<Til> tils = List.of(TilFactory.create(profile, null, FactoryPreset.CONTENT));

        // When, Then
        assertThrows(DataIntegrityViolationException.class, () -> tilRepository.saveAll(tils));
    }

    @Test
    @DisplayName("[Fail] content is null")
    void failIfContentIsNull() {
        // Given
        final List<Til> tils = List.of(TilFactory.create(profile, FactoryPreset.TITLE, null));

        // When, Then
        assertThrows(DataIntegrityViolationException.class, () -> tilRepository.saveAll(tils));
    }

    @Test
    @DisplayName("[Success] til is acceptable")
    void successIfTilIsAcceptable() {
        // Given
        final Til til = TilFactory.createDefaultTil(profile);

        // When
        final List<Til> result = tilRepository.saveAll(List.of(til));

        // Then
        assertThat(result.get(0).getId()).isNotNull();
        assertThat(result.get(0).getProfile()).isEqualTo(til.getProfile());
        assertThat(result.get(0).getUuid()).hasSize(36);
        assertThat(result.get(0).getTitle()).isEqualTo(til.getTitle());
        assertThat(result.get(0).getContent()).isEqualTo(til.getContent());
    }
}