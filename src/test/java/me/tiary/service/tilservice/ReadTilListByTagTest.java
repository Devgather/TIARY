package me.tiary.service.tilservice;

import common.annotation.service.ServiceTest;
import common.config.factory.FactoryPreset;
import common.factory.domain.ProfileFactory;
import common.factory.domain.TagFactory;
import common.factory.domain.TilFactory;
import common.factory.domain.TilTagFactory;
import me.tiary.domain.Profile;
import me.tiary.domain.Tag;
import me.tiary.domain.Til;
import me.tiary.domain.TilTag;
import me.tiary.dto.til.TilListReadResponseDto;
import me.tiary.exception.TilException;
import me.tiary.exception.status.TilStatus;
import me.tiary.repository.ProfileRepository;
import me.tiary.repository.TilTagRepository;
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
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;

@ServiceTest
@DisplayName("[TilService] readTilListByTag")
class ReadTilListByTagTest {
    @InjectMocks
    private TilService tilService;

    @Mock
    private TilTagRepository tilTagRepository;

    @Mock
    private ProfileRepository profileRepository;

    @Spy
    private ModelMapper modelMapper;

    @BeforeEach
    void beforeEach() {
        modelMapper.getConfiguration()
                .setFieldMatchingEnabled(true)
                .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE);
    }

    @Test
    @DisplayName("[Fail] profile does not exist")
    void failIfProfileDoesNotExist() {
        // Given
        doReturn(Optional.empty())
                .when(profileRepository)
                .findByNickname(FactoryPreset.NICKNAME);

        final Pageable pageable = PageRequest.of(0, 5, Sort.by("createdDate").descending());

        // When, Then
        final TilException result = assertThrows(TilException.class, () -> tilService.readTilListByTag(FactoryPreset.NICKNAME, FactoryPreset.TAG, pageable));

        assertThat(result.getStatus()).isEqualTo(TilStatus.NOT_EXISTING_PROFILE);
    }

    @Test
    @DisplayName("[Fail] tag does not exist")
    void failIfTagDoesNotExist() {
        // Given
        final Profile profile = ProfileFactory.createDefaultProfile();

        final String nickname = profile.getNickname();

        doReturn(Optional.of(profile))
                .when(profileRepository)
                .findByNickname(nickname);

        final Pageable pageable = PageRequest.of(0, 5, Sort.by("createdDate").descending());
        final Page<TilTag> tilTagPage = new PageImpl<>(new ArrayList<>(), pageable, 0);

        doReturn(tilTagPage)
                .when(tilTagRepository)
                .findJoinFetchTilByTilProfileNicknameAndTagName(nickname, FactoryPreset.TAG, pageable);

        // When, Then
        final TilException result = assertThrows(TilException.class, () -> tilService.readTilListByTag(nickname, FactoryPreset.TAG, pageable));

        assertThat(result.getStatus()).isEqualTo(TilStatus.NOT_EXISTING_TAG);
    }

    @Test
    @DisplayName("[Success] tils do exist")
    void successIfTilsDoExist() {
        // Given
        final Profile profile = ProfileFactory.createDefaultProfile();

        final String nickname = profile.getNickname();

        doReturn(Optional.of(profile))
                .when(profileRepository)
                .findByNickname(nickname);

        final Til til = TilFactory.createDefaultTil(profile);
        final Tag tag = TagFactory.createDefaultTag();
        final List<TilTag> tilTags = List.of(TilTagFactory.create(til, tag));

        final String tagName = tag.getName();

        final Pageable pageable = PageRequest.of(0, 5, Sort.by("createdDate").descending());
        final Page<TilTag> tilTagPage = new PageImpl<>(tilTags, pageable, tilTags.size());

        doReturn(tilTagPage)
                .when(tilTagRepository)
                .findJoinFetchTilByTilProfileNicknameAndTagName(nickname, tagName, pageable);

        // When
        final TilListReadResponseDto result = tilService.readTilListByTag(nickname, tagName, pageable);

        // Then
        assertThat(result.getTils().get(0).getUuid()).hasSize(36);
        assertThat(result.getTils().get(0).getTitle()).isEqualTo(til.getTitle());
        assertThat(result.getTils().get(0).getContent()).isEqualTo(til.getContent());
        assertThat(result.getTotalPages()).isEqualTo(tilTagPage.getTotalPages());
    }
}