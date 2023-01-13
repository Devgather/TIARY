package me.tiary.repository.tiltagrepository;

import common.annotation.repository.RepositoryIntegrationTest;
import common.factory.domain.ProfileFactory;
import common.factory.domain.TagFactory;
import common.factory.domain.TilFactory;
import common.factory.domain.TilTagFactory;
import common.utility.JpaUtility;
import me.tiary.domain.Profile;
import me.tiary.domain.Tag;
import me.tiary.domain.Til;
import me.tiary.domain.TilTag;
import me.tiary.repository.ProfileRepository;
import me.tiary.repository.TagRepository;
import me.tiary.repository.TilRepository;
import me.tiary.repository.TilTagRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.JpaSystemException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@RepositoryIntegrationTest
@DisplayName("[TilTagRepository - Integration] saveAll")
class SaveAllIntegrationTest {
    @Autowired
    private TilTagRepository tilTagRepository;

    @Autowired
    private TilRepository tilRepository;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private ProfileRepository profileRepository;

    @PersistenceContext
    private EntityManager em;

    private Til til;

    private Tag tag;

    @BeforeEach
    void beforeEach() {
        final Profile profile = profileRepository.save(ProfileFactory.createDefaultProfile());

        til = tilRepository.save(TilFactory.createDefaultTil(profile));

        tag = tagRepository.save(TagFactory.createDefaultTag());

        JpaUtility.flushAndClear(em);
    }

    @Test
    @DisplayName("[Fail] til is null and tag is null")
    void failIfTilIsNullAndTagIsNull() {
        // Given
        final TilTag tilTag = TilTagFactory.create(null, null);

        // When, Then
        assertThrows(JpaSystemException.class, () -> tilTagRepository.saveAll(List.of(tilTag)));
    }

    @Test
    @DisplayName("[Fail] til is not null and tag is null")
    void failIfTilIsNotNullAndTagIsNull() {
        // Given
        final TilTag tilTag = TilTagFactory.create(til, null);

        // When, Then
        assertThrows(JpaSystemException.class, () -> tilTagRepository.saveAll(List.of(tilTag)));
    }

    @Test
    @DisplayName("[Fail] til is null and tag is not null")
    void failIfTilIsNullAndTagIsNotNull() {
        // Given
        final TilTag tilTag = TilTagFactory.create(null, tag);

        // When, Then
        assertThrows(JpaSystemException.class, () -> tilTagRepository.saveAll(List.of(tilTag)));
    }

    @Test
    @DisplayName("[Success] til is not null and tag is not null")
    void successIfTilIsNotNullAndTagIsNotNull() {
        // Given
        final TilTag tilTag = TilTagFactory.create(til, tag);

        // When
        final List<TilTag> result = tilTagRepository.saveAll(List.of(tilTag));

        // Then
        assertThat(result.get(0).getId()).isNotNull();
        assertThat(result.get(0).getUuid().length()).isEqualTo(36);
        assertThat(result.get(0).getTil()).isEqualTo(tilTag.getTil());
        assertThat(result.get(0).getTag()).isEqualTo(tilTag.getTag());
    }
}