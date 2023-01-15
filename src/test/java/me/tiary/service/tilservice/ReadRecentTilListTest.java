package me.tiary.service.tilservice;

import common.annotation.service.ServiceTest;
import common.factory.domain.ProfileFactory;
import common.factory.domain.TilFactory;
import me.tiary.domain.Profile;
import me.tiary.domain.Til;
import me.tiary.dto.til.RecentTilListReadResponseDto;
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

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;

@ServiceTest
@DisplayName("[TilService] readRecentTilList")
class ReadRecentTilListTest {
    @InjectMocks
    private TilService tilService;

    @Mock
    private TilRepository tilRepository;

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
    @DisplayName("[Success] tils do not exist")
    void successIfTilsDoNotExist() {
        // Given
        final Pageable pageable = PageRequest.of(0, 5, Sort.by("createdDate").descending());

        final Page<Til> tilPage = new PageImpl<>(new ArrayList<>(), pageable, 0);

        doReturn(tilPage)
                .when(tilRepository)
                .findAll(pageable);

        // When
        final RecentTilListReadResponseDto result = tilService.readRecentTilList(pageable);

        // Then
        assertThat(result.getTils()).isEmpty();
    }

    @Test
    @DisplayName("[Success] tils do exist")
    void successIfTilsDoExist() {
        // Given
        final List<Til> tils = List.of(TilFactory.createDefaultTil(profile));

        final Pageable pageable = PageRequest.of(0, 5, Sort.by("createdDate").descending());

        final Page<Til> tilPage = new PageImpl<>(tils, pageable, tils.size());

        doReturn(tilPage)
                .when(tilRepository)
                .findAll(pageable);

        // When
        final RecentTilListReadResponseDto result = tilService.readRecentTilList(pageable);

        // Then
        assertThat(result.getTils().get(0).getUuid()).hasSize(36);
        assertThat(result.getTils().get(0).getTitle()).isEqualTo(tils.get(0).getTitle());
        assertThat(result.getTils().get(0).getContent()).isEqualTo(tils.get(0).getContent());
    }
}