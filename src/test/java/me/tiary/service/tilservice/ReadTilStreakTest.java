package me.tiary.service.tilservice;

import common.annotation.service.ServiceTest;
import common.factory.domain.ProfileFactory;
import common.factory.domain.TilFactory;
import me.tiary.domain.Profile;
import me.tiary.domain.Til;
import me.tiary.dto.til.TilStreakReadResponseDto;
import me.tiary.exception.TilException;
import me.tiary.exception.status.TilStatus;
import me.tiary.repository.ProfileRepository;
import me.tiary.repository.TilRepository;
import me.tiary.service.TilService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;

@ServiceTest
@DisplayName("[TilService] readTilStreak")
class ReadTilStreakTest {
    @InjectMocks
    private TilService tilService;

    @Mock
    private TilRepository tilRepository;

    @Mock
    private ProfileRepository profileRepository;

    private Profile profile;

    @BeforeEach
    void beforeEach() {
        profile = ProfileFactory.createDefaultProfile();
    }

    @Test
    @DisplayName("[Fail] profile does not exist")
    void failIfProfileDoesNotExist() {
        // Given
        final String nickname = profile.getNickname();

        doReturn(Optional.empty())
                .when(profileRepository)
                .findByNickname(nickname);

        final LocalDate startDate = LocalDate.now();
        final LocalDate endDate = LocalDate.now();

        // When, Then
        final TilException result = assertThrows(TilException.class, () -> tilService.readTilStreak(nickname, startDate, endDate));

        assertThat(result.getStatus()).isEqualTo(TilStatus.NOT_EXISTING_PROFILE);
    }

    @Test
    @DisplayName("[Success] tils do not exist")
    void successIfTilsDoNotExist() {
        // Given
        doReturn(Optional.of(profile))
                .when(profileRepository)
                .findByNickname(profile.getNickname());

        final List<Til> tils = new ArrayList<>();
        final LocalDate startDate = LocalDate.now();
        final LocalDate endDate = LocalDate.now();

        doReturn(tils)
                .when(tilRepository)
                .findAllByProfileNicknameAndCreatedDateBetween(profile.getNickname(), startDate.atTime(LocalTime.MIN), endDate.atTime(LocalTime.MAX));

        // When
        final TilStreakReadResponseDto result = tilService.readTilStreak(profile.getNickname(), startDate, endDate);

        // Then
        assertThat(result.getStreaks()).isEmpty();
    }

    @Test
    @DisplayName("[Success] tils do exist")
    void successIfTilsDoExist() {
        // Given
        doReturn(Optional.of(profile))
                .when(profileRepository)
                .findByNickname(profile.getNickname());

        final Til til = TilFactory.createDefaultTil(profile);

        til.markCreated();
        til.markModified();

        final List<Til> tils = List.of(til, til, til);
        final LocalDate startDate = LocalDate.now();
        final LocalDate endDate = LocalDate.now();

        doReturn(tils)
                .when(tilRepository)
                .findAllByProfileNicknameAndCreatedDateBetween(profile.getNickname(), startDate.atTime(LocalTime.MIN), endDate.atTime(LocalTime.MAX));

        // When
        final TilStreakReadResponseDto result = tilService.readTilStreak(profile.getNickname(), startDate, endDate);

        // Then
        assertThat(result.getStreaks().get(0).getDate()).isEqualTo(til.getCreatedDate().toLocalDate());
        assertThat(result.getStreaks().get(0).getTilNumber()).isEqualTo(3);
    }
}