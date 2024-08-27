package me.tiary.service.tagservice;

import common.annotation.service.ServiceTest;
import common.config.factory.FactoryPreset;
import common.factory.domain.ProfileFactory;
import common.factory.domain.TagFactory;
import common.factory.domain.TilFactory;
import common.factory.domain.TilTagFactory;
import me.tiary.domain.Profile;
import me.tiary.domain.Til;
import me.tiary.domain.TilTag;
import me.tiary.dto.tag.TagListReadResponseDto;
import me.tiary.exception.TagException;
import me.tiary.exception.status.TagStatus;
import me.tiary.repository.ProfileRepository;
import me.tiary.repository.TilTagRepository;
import me.tiary.service.TagService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;

@ServiceTest
@DisplayName("[TagService] readTagListByProfile")
class ReadTagListByProfileTest {
    @InjectMocks
    private TagService tagService;

    @Mock
    private TilTagRepository tilTagRepository;

    @Mock
    private ProfileRepository profileRepository;

    private Profile profile;

    private String nickname;

    @BeforeEach
    void beforeEach() {
        profile = ProfileFactory.createDefaultProfile();

        nickname = profile.getNickname();
    }

    @Test
    @DisplayName("[Fail] profile does not exist")
    void failIfProfileDoesNotExist() {
        // Given
        doReturn(Optional.empty())
                .when(profileRepository)
                .findByNickname(nickname);

        // When, Then
        final TagException result = assertThrows(TagException.class, () -> tagService.readTagListByProfile(nickname));

        assertThat(result.getStatus()).isEqualTo(TagStatus.NOT_EXISTING_PROFILE);
    }

    @Test
    @DisplayName("[Success] tags do not exist")
    void successIfTagsDoNotExist() {
        // Given
        doReturn(Optional.of(profile))
                .when(profileRepository)
                .findByNickname(nickname);

        doReturn(new ArrayList<>())
                .when(tilTagRepository)
                .findAllByTilProfileNicknameJoinFetchTag(nickname);

        // When
        final TagListReadResponseDto result = tagService.readTagListByProfile(nickname);

        // Then
        assertThat(result.getTags()).isEmpty();
    }

    @Test
    @DisplayName("[Success] tags do exist")
    void successIfTagsDoExist() {
        // Given
        doReturn(Optional.of(profile))
                .when(profileRepository)
                .findByNickname(nickname);

        final Til til = TilFactory.createDefaultTil(profile);

        final List<TilTag> tilTags = List.of(
                TilTagFactory.create(til, TagFactory.create(FactoryPreset.TAGS.get(0))),
                TilTagFactory.create(til, TagFactory.create(FactoryPreset.TAGS.get(1)))
        );

        doReturn(tilTags)
                .when(tilTagRepository)
                .findAllByTilProfileNicknameJoinFetchTag(nickname);

        // When
        final TagListReadResponseDto result = tagService.readTagListByProfile(nickname);

        // Then
        assertThat(result.getTags()).hasSize(2);
        assertThat(result.getTags().get(0)).isEqualTo(tilTags.get(0).getTag().getName());
        assertThat(result.getTags().get(1)).isEqualTo(tilTags.get(1).getTag().getName());
    }
}