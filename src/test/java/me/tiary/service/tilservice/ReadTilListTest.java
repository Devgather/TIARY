package me.tiary.service.tilservice;

import common.annotation.service.ServiceTest;
import common.factory.domain.ProfileFactory;
import common.factory.domain.TilFactory;
import me.tiary.domain.Profile;
import me.tiary.domain.Til;
import me.tiary.dto.til.TilListReadResponseDto;
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
import org.mockito.Spy;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.*;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;

@ServiceTest
@DisplayName("[TilService] readTilList")
class ReadTilListTest {
    @InjectMocks
    private TilService tilService;

    @Mock
    private TilRepository tilRepository;

    @Mock
    private ProfileRepository profileRepository;

    @Spy
    private ModelMapper modelMapper;

    private Profile profile;

    @BeforeEach
    void beforeEach() {
        modelMapper.getConfiguration()
                .setFieldMatchingEnabled(true)
                .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE);

        profile = ProfileFactory.createDefaultProfile();
    }

    @Test
    @DisplayName("[Fail] profile does not exist")
    void failIfProfileDoesNotExist() {
        // Given
        doReturn(Optional.empty())
                .when(profileRepository)
                .findByNickname(profile.getNickname());

        final Pageable pageable = PageRequest.of(0, 5, Sort.by("createdDate").descending());

        // When, Then
        final TilException result = assertThrows(TilException.class, () -> tilService.readTilList(profile.getNickname(), pageable));

        assertThat(result.getStatus()).isEqualTo(TilStatus.NOT_EXISTING_PROFILE);
    }

    @Test
    @DisplayName("[Success] tils do exist")
    void successIfTilsDoExist() {
        // Given
        doReturn(Optional.of(profile))
                .when(profileRepository)
                .findByNickname(profile.getNickname());

        final List<Til> tils = List.of(TilFactory.createDefaultTil(profile));

        final Pageable pageable = PageRequest.of(0, 5, Sort.by("createdDate").descending());

        final Page<Til> tilPage = new PageImpl<>(tils, pageable, tils.size());

        doReturn(tilPage)
                .when(tilRepository)
                .findByProfileNickname(profile.getNickname(), pageable);

        // When
        final TilListReadResponseDto result = tilService.readTilList(profile.getNickname(), pageable);

        // Then
        assertThat(result.getTils().get(0).getUuid()).hasSize(36);
        assertThat(result.getTils().get(0).getTitle()).isEqualTo(tils.get(0).getTitle());
        assertThat(result.getTils().get(0).getContent()).isEqualTo(tils.get(0).getContent());
    }
}
