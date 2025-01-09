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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RepositoryIntegrationTest
@DisplayName("[TilTagRepository - Integration] findJoinFetchTilByTilProfileNicknameAndTagName")
class FindJoinFetchTilByTilProfileNicknameAndTagNameIntegrationTest {
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

    private Profile profile;

    private Tag tag;

    @BeforeEach
    void beforeEach() {
        profile = profileRepository.save(ProfileFactory.createDefaultProfile());

        tag = tagRepository.save(TagFactory.createDefaultTag());

        JpaUtility.flushAndClear(em);
    }

    @Test
    @DisplayName("[Success] til tags for profile do not exist")
    void successIfTilTagsForProfileDoNotExist() {
        // Given
        final Profile someoneElseProfile = profileRepository.save(ProfileFactory.create("Someone else " + profile.getNickname(), FactoryPreset.PICTURE));

        final List<TilTag> tilTags = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            final Til til = tilRepository.save(TilFactory.createDefaultTil(someoneElseProfile));

            tilTags.add(TilTagFactory.create(til, tag));
        }

        tilTagRepository.saveAll(tilTags);

        JpaUtility.flushAndClear(em);

        final Pageable pageable = PageRequest.of(0, 5, Sort.by("createdDate").descending());

        // When
        final Page<TilTag> result = tilTagRepository.findJoinFetchTilByTilProfileNicknameAndTagName(profile.getNickname(), tag.getName(), pageable);

        // Then
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("[Success] til tags for tag do not exist")
    void successIfTilTagsForTagDoNotExist() {
        // Given
        final Tag otherTag = tagRepository.save(TagFactory.create("Other " + tag.getName()));

        final List<TilTag> tilTags = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            final Til til = tilRepository.save(TilFactory.createDefaultTil(profile));

            tilTags.add(TilTagFactory.create(til, otherTag));
        }

        tilTagRepository.saveAll(tilTags);

        JpaUtility.flushAndClear(em);

        final Pageable pageable = PageRequest.of(0, 5, Sort.by("createdDate").descending());

        // When
        final Page<TilTag> result = tilTagRepository.findJoinFetchTilByTilProfileNicknameAndTagName(profile.getNickname(), tag.getName(), pageable);

        // Then
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("[Success] til tags for profile and tag do not exist")
    void successIfTilTagsForProfileAndTagDoNotExist() {
        // Given
        final Profile someoneElseProfile = profileRepository.save(ProfileFactory.create("Someone else " + profile.getNickname(), FactoryPreset.PICTURE));
        final Tag otherTag = tagRepository.save(TagFactory.create("Other " + tag.getName()));

        final List<TilTag> tilTags = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            final Til til = tilRepository.save(TilFactory.createDefaultTil(someoneElseProfile));

            tilTags.add(TilTagFactory.create(til, otherTag));
        }

        tilTagRepository.saveAll(tilTags);

        JpaUtility.flushAndClear(em);

        final Pageable pageable = PageRequest.of(0, 5, Sort.by("createdDate").descending());

        // When
        final Page<TilTag> result = tilTagRepository.findJoinFetchTilByTilProfileNicknameAndTagName(profile.getNickname(), tag.getName(), pageable);

        // Then
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("[Success] til tag number does not meet request")
    void successIfTilTagNumberDoesNotMeetRequest() {
        // Given
        final List<TilTag> tilTags = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            final Til til = tilRepository.save(TilFactory.createDefaultTil(profile));

            tilTags.add(TilTagFactory.create(til, tag));
        }

        tilTagRepository.saveAll(tilTags);

        JpaUtility.flushAndClear(em);

        final Pageable pageable = PageRequest.of(0, 5, Sort.by("createdDate").descending());

        // When
        final Page<TilTag> result = tilTagRepository.findJoinFetchTilByTilProfileNicknameAndTagName(profile.getNickname(), tag.getName(), pageable);

        // Then
        assertThat(result.getTotalPages()).isEqualTo(1);
        assertThat(result.getContent()).hasSize(3);
    }

    @Test
    @DisplayName("[Success] til tag number does meet request")
    void successIfTilTagNumberDoesMeetRequest() {
        // Given
        final List<TilTag> tilTags = new ArrayList<>();

        for (int i = 0; i < 13; i++) {
            final Til til = tilRepository.save(TilFactory.createDefaultTil(profile));

            tilTags.add(TilTagFactory.create(til, tag));
        }

        tilTagRepository.saveAll(tilTags);

        JpaUtility.flushAndClear(em);

        final Pageable pageable1 = PageRequest.of(0, 5, Sort.by("createdDate").descending());
        final Pageable pageable2 = PageRequest.of(1, 5, Sort.by("createdDate").descending());
        final Pageable pageable3 = PageRequest.of(2, 5, Sort.by("createdDate").descending());

        // When
        final Page<TilTag> result1 = tilTagRepository.findJoinFetchTilByTilProfileNicknameAndTagName(profile.getNickname(), tag.getName(), pageable1);
        final Page<TilTag> result2 = tilTagRepository.findJoinFetchTilByTilProfileNicknameAndTagName(profile.getNickname(), tag.getName(), pageable2);
        final Page<TilTag> result3 = tilTagRepository.findJoinFetchTilByTilProfileNicknameAndTagName(profile.getNickname(), tag.getName(), pageable3);

        // Then
        assertThat(result1.getTotalPages()).isEqualTo(3);
        assertThat(result2.getTotalPages()).isEqualTo(3);
        assertThat(result3.getTotalPages()).isEqualTo(3);
        assertThat(result1.getContent()).hasSize(5);
        assertThat(result2.getContent()).hasSize(5);
        assertThat(result3.getContent()).hasSize(3);
    }
}