package me.tiary.service.tagservice;

import common.annotation.service.ServiceTest;
import common.config.factory.FactoryPreset;
import common.factory.domain.ProfileFactory;
import common.factory.domain.TagFactory;
import common.factory.domain.TilFactory;
import common.factory.domain.TilTagFactory;
import common.factory.dto.tag.TagListEditRequestDtoFactory;
import me.tiary.domain.Profile;
import me.tiary.domain.Tag;
import me.tiary.domain.Til;
import me.tiary.domain.TilTag;
import me.tiary.dto.tag.TagListEditRequestDto;
import me.tiary.exception.TagException;
import me.tiary.exception.status.TagStatus;
import me.tiary.repository.TagRepository;
import me.tiary.repository.TilRepository;
import me.tiary.repository.TilTagRepository;
import me.tiary.service.TagService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;

@ServiceTest
@DisplayName("[TagService] updateTagList")
class UpdateTagListTest {
    @InjectMocks
    private TagService tagService;

    @Mock
    private TagRepository tagRepository;

    @Mock
    private TilRepository tilRepository;

    @Mock
    private TilTagRepository tilTagRepository;

    @Test
    @DisplayName("[Fail] til does not exist")
    void failIfTilDoesNotExist() {
        // Given
        final String profileUuid = UUID.randomUUID().toString();

        final String tilUuid = UUID.randomUUID().toString();

        doReturn(Optional.empty())
                .when(tilRepository)
                .findByUuidJoinFetchProfile(tilUuid);

        final TagListEditRequestDto requestDto = TagListEditRequestDtoFactory.createDefaultTagListEditRequestDto();

        // When, Then
        final TagException result = assertThrows(TagException.class, () -> tagService.updateTagList(profileUuid, tilUuid, requestDto));

        assertThat(result.getStatus()).isEqualTo(TagStatus.NOT_EXISTING_TIL);
    }

    @Test
    @DisplayName("[Fail] member does not have authorization")
    void failIfMemberDoesNotHaveAuthorization() {
        // Given
        final Profile profile = ProfileFactory.createDefaultProfile();

        final String profileUuid = UUID.randomUUID().toString();

        final Til til = TilFactory.createDefaultTil(profile);

        final String tilUuid = til.getUuid();

        doReturn(Optional.of(til))
                .when(tilRepository)
                .findByUuidJoinFetchProfile(tilUuid);

        final TagListEditRequestDto requestDto = TagListEditRequestDtoFactory.createDefaultTagListEditRequestDto();

        // When, Then
        final TagException result = assertThrows(TagException.class, () -> tagService.updateTagList(profileUuid, tilUuid, requestDto));

        assertThat(result.getStatus()).isEqualTo(TagStatus.NOT_AUTHORIZED_MEMBER);
    }

    @Test
    @DisplayName("[Success] tags are acceptable")
    void successIfTagsAreAcceptable() {
        // Given
        final Profile profile = ProfileFactory.createDefaultProfile();

        final String profileUuid = profile.getUuid();

        final Til til = TilFactory.createDefaultTil(profile);

        final String tilUuid = til.getUuid();

        doReturn(Optional.of(til))
                .when(tilRepository)
                .findByUuidJoinFetchProfile(tilUuid);

        doNothing()
                .when(tilTagRepository)
                .deleteAllByTilUuid(tilUuid);

        final Tag tag1 = TagFactory.create(FactoryPreset.TAGS.get(0));

        doReturn(Optional.of(tag1))
                .when(tagRepository)
                .findByName(tag1.getName());

        final Tag tag2 = TagFactory.create(FactoryPreset.TAGS.get(1));

        doReturn(Optional.empty())
                .when(tagRepository)
                .findByName(tag2.getName());

        doReturn(tag2)
                .when(tagRepository)
                .save(any(Tag.class));

        final List<TilTag> tilTags = List.of(TilTagFactory.create(til, tag1), TilTagFactory.create(til, tag2));

        doReturn(tilTags)
                .when(tilTagRepository)
                .saveAll(any());

        final TagListEditRequestDto requestDto = TagListEditRequestDtoFactory.createDefaultTagListEditRequestDto();

        // When, Then
        assertDoesNotThrow(() -> tagService.updateTagList(profileUuid, tilUuid, requestDto));
    }
}