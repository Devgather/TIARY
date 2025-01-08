package me.tiary.service.tagservice;

import common.annotation.service.ServiceTest;
import common.config.factory.FactoryPreset;
import common.factory.domain.ProfileFactory;
import common.factory.domain.TagFactory;
import common.factory.domain.TilFactory;
import common.factory.domain.TilTagFactory;
import me.tiary.domain.Til;
import me.tiary.domain.TilTag;
import me.tiary.dto.tag.TagListReadResponseDto;
import me.tiary.exception.TagException;
import me.tiary.exception.status.TagStatus;
import me.tiary.repository.TilRepository;
import me.tiary.repository.TilTagRepository;
import me.tiary.service.TagService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;

@ServiceTest
@DisplayName("[TagService] readTagList")
class ReadTagListTest {
    @InjectMocks
    private TagService tagService;

    @Mock
    private TilRepository tilRepository;

    @Mock
    private TilTagRepository tilTagRepository;

    @Test
    @DisplayName("[Fail] til does not exist")
    void failIfTilDoesNotExist() {
        // Given
        final String tilUuid = UUID.randomUUID().toString();

        doReturn(Optional.empty())
                .when(tilRepository)
                .findByUuid(tilUuid);

        // When, Then
        final TagException result = assertThrows(TagException.class, () -> tagService.readTagList(tilUuid));

        assertThat(result.getStatus()).isEqualTo(TagStatus.NOT_EXISTING_TIL);
    }

    @Test
    @DisplayName("[Success] tags do not exist")
    void successIfTagsDoNotExist() {
        // Given
        final String tilUuid = UUID.randomUUID().toString();

        final Til til = TilFactory.createDefaultTil(ProfileFactory.createDefaultProfile());

        doReturn(Optional.of(til))
                .when(tilRepository)
                .findByUuid(tilUuid);

        doReturn(new ArrayList<>())
                .when(tilTagRepository)
                .findAllJoinFetchTagByTilUuid(tilUuid);

        // When
        final TagListReadResponseDto result = tagService.readTagList(tilUuid);

        // Then
        assertThat(result.getTags()).isEmpty();
    }

    @Test
    @DisplayName("[Success] tags do exist")
    void successIfTagsDoExist() {
        // Given
        final String tilUuid = UUID.randomUUID().toString();

        final Til til = TilFactory.createDefaultTil(ProfileFactory.createDefaultProfile());

        doReturn(Optional.of(til))
                .when(tilRepository)
                .findByUuid(tilUuid);

        final List<TilTag> tilTags = List.of(
                TilTagFactory.create(til, TagFactory.create(FactoryPreset.TAGS.get(0))),
                TilTagFactory.create(til, TagFactory.create(FactoryPreset.TAGS.get(1)))
        );

        doReturn(tilTags)
                .when(tilTagRepository)
                .findAllJoinFetchTagByTilUuid(tilUuid);

        // When
        final TagListReadResponseDto result = tagService.readTagList(tilUuid);

        // Then
        assertThat(result.getTags()).hasSize(2);
        assertThat(result.getTags().get(0)).isEqualTo(tilTags.get(0).getTag().getName());
        assertThat(result.getTags().get(1)).isEqualTo(tilTags.get(1).getTag().getName());
    }
}