package me.tiary.repository.tilrepository;

import common.annotation.repository.RepositoryIntegrationTest;
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
@DisplayName("[TilRepository - Integration] findJoinFetchProfile")
class FindJoinFetchProfileIntegrationTest {
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
    @DisplayName("[Success] til does not exist")
    void successIfTilDoesNotExist() {
        // Given
        final Pageable pageable = PageRequest.of(0, 5, Sort.by("createdDate").descending());

        // When
        final Page<Til> result = tilRepository.findJoinFetchProfile(pageable);

        // Then
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("[Success] til number does not meet request")
    void successIfTilNumberDoesNotMeetRequest() {
        // Given
        final List<Til> tils = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            tils.add(TilFactory.createDefaultTil(profile));
        }

        tilRepository.saveAll(tils);

        JpaUtility.flushAndClear(em);

        final Pageable pageable = PageRequest.of(0, 5, Sort.by("createdDate").descending());

        // When
        final Page<Til> result = tilRepository.findJoinFetchProfile(pageable);

        // Then
        assertThat(result.getTotalPages()).isEqualTo(1);
        assertThat(result.getContent()).hasSize(3);
    }

    @Test
    @DisplayName("[Success] til number does meet request")
    void successIfTilNumberDoesMeetRequest() {
        // Given
        final List<Til> tils = new ArrayList<>();

        for (int i = 0; i < 13; i++) {
            tils.add(TilFactory.createDefaultTil(profile));
        }

        tilRepository.saveAll(tils);

        JpaUtility.flushAndClear(em);

        final Pageable pageable1 = PageRequest.of(0, 5, Sort.by("createdDate").descending());
        final Pageable pageable2 = PageRequest.of(1, 5, Sort.by("createdDate").descending());
        final Pageable pageable3 = PageRequest.of(2, 5, Sort.by("createdDate").descending());

        // When
        final Page<Til> result1 = tilRepository.findJoinFetchProfile(pageable1);
        final Page<Til> result2 = tilRepository.findJoinFetchProfile(pageable2);
        final Page<Til> result3 = tilRepository.findJoinFetchProfile(pageable3);

        // Then
        assertThat(result1.getTotalPages()).isEqualTo(3);
        assertThat(result2.getTotalPages()).isEqualTo(3);
        assertThat(result3.getTotalPages()).isEqualTo(3);
        assertThat(result1.getContent()).hasSize(5);
        assertThat(result2.getContent()).hasSize(5);
        assertThat(result3.getContent()).hasSize(3);
    }
}