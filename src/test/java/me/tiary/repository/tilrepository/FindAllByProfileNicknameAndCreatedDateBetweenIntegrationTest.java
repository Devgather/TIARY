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

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RepositoryIntegrationTest
@DisplayName("[TilRepository - Integration] findAllByProfileNicknameAndCreatedDateBetween")
class FindAllByProfileNicknameAndCreatedDateBetweenIntegrationTest {
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
        final LocalDateTime startDate = LocalDateTime.now();
        final LocalDateTime endDate = LocalDateTime.now();

        // When
        final List<Til> result = tilRepository.findAllByProfileNicknameAndCreatedDateBetween(profile.getNickname(), startDate, endDate);

        // Then
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("[Success] til corresponding to profile nickname does not exist")
    void successIfTilCorrespondingToProfileNicknameDoesNotExist() {
        // Given
        final LocalDateTime startDate = LocalDateTime.now();

        final List<Til> tils = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            tils.add(TilFactory.createDefaultTil(profile));
        }

        tilRepository.saveAll(tils);

        JpaUtility.flushAndClear(em);

        final String thirdPartyProfileNickname = "ThirdParty" + profile.getNickname();
        final LocalDateTime endDate = LocalDateTime.now();

        // When
        final List<Til> result = tilRepository.findAllByProfileNicknameAndCreatedDateBetween(thirdPartyProfileNickname, startDate, endDate);

        // Then
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("[Success] til for period does not exist")
    void successIfTilForPeriodDoesNotExist() {
        // Given
        final List<Til> tils = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            tils.add(TilFactory.createDefaultTil(profile));
        }

        tilRepository.saveAll(tils);

        JpaUtility.flushAndClear(em);

        final LocalDateTime startDate = LocalDateTime.now();
        final LocalDateTime endDate = LocalDateTime.now();

        // When
        final List<Til> result = tilRepository.findAllByProfileNicknameAndCreatedDateBetween(profile.getNickname(), startDate, endDate);

        // Then
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("[Success] til does exist")
    void successIfTilDoesExist() {
        // Given
        final List<Til> outPeriodTils = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            outPeriodTils.add(TilFactory.createDefaultTil(profile));
        }

        tilRepository.saveAll(outPeriodTils);

        JpaUtility.flushAndClear(em);

        final LocalDateTime startDate = LocalDateTime.now();

        final List<Til> inPeriodTils = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            inPeriodTils.add(TilFactory.createDefaultTil(profile));
        }

        tilRepository.saveAll(inPeriodTils);

        JpaUtility.flushAndClear(em);

        final LocalDateTime endDate = LocalDateTime.now();

        // When
        final List<Til> result = tilRepository.findAllByProfileNicknameAndCreatedDateBetween(profile.getNickname(), startDate, endDate);

        // Then
        assertThat(result).hasSize(3);
    }
}