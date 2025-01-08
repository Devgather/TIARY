package me.tiary.repository.tiltagrepository;

import common.annotation.repository.RepositoryIntegrationTest;
import common.config.factory.FactoryPreset;
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

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RepositoryIntegrationTest
@DisplayName("[TilTagRepository - Integration] findAllJoinFetchTagByTilUuid")
class FindAllJoinFetchTagByTilUuidIntegrationTest {
    @Autowired
    private TilTagRepository tilTagRepository;

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private TilRepository tilRepository;

    @Autowired
    private TagRepository tagRepository;

    @PersistenceContext
    private EntityManager em;

    private Til til;

    @BeforeEach
    void beforeEach() {
        final Profile profile = profileRepository.save(ProfileFactory.createDefaultProfile());

        til = tilRepository.save(TilFactory.createDefaultTil(profile));

        JpaUtility.flushAndClear(em);
    }

    @Test
    @DisplayName("[Success] til tags do not exist")
    void successIfTilTagsDoNotExist() {
        // When
        final List<TilTag> result = tilTagRepository.findAllJoinFetchTagByTilUuid(til.getUuid());

        // Then
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("[Success] til tags do exist")
    void successIfTilTagsDoExist() {
        // Given
        final List<TilTag> tilTags = new ArrayList<>();

        final Tag tag1 = tagRepository.save(TagFactory.create(FactoryPreset.TAGS.get(0)));
        final Tag tag2 = tagRepository.save(TagFactory.create(FactoryPreset.TAGS.get(1)));

        tilTags.add(TilTagFactory.create(til, tag1));
        tilTags.add(TilTagFactory.create(til, tag2));

        tilTagRepository.saveAll(tilTags);

        JpaUtility.flushAndClear(em);

        // When
        final List<TilTag> result = tilTagRepository.findAllJoinFetchTagByTilUuid(til.getUuid());

        // Then
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getTil()).isEqualTo(til);
        assertThat(result.get(0).getTag()).isEqualTo(tag1);
        assertThat(result.get(1).getTil()).isEqualTo(til);
        assertThat(result.get(1).getTag()).isEqualTo(tag2);
    }
}