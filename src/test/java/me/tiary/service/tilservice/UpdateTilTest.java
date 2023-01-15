package me.tiary.service.tilservice;

import common.annotation.service.ServiceTest;
import common.config.factory.FactoryPreset;
import common.factory.domain.ProfileFactory;
import common.factory.domain.TagFactory;
import common.factory.domain.TilFactory;
import common.factory.domain.TilTagFactory;
import common.factory.dto.til.TilUpdateRequestDtoFactory;
import me.tiary.domain.Profile;
import me.tiary.domain.Tag;
import me.tiary.domain.Til;
import me.tiary.domain.TilTag;
import me.tiary.dto.til.TilUpdateRequestDto;
import me.tiary.dto.til.TilUpdateResponseDto;
import me.tiary.exception.TilException;
import me.tiary.exception.status.TilStatus;
import me.tiary.repository.TagRepository;
import me.tiary.repository.TilRepository;
import me.tiary.repository.TilTagRepository;
import me.tiary.service.TilService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

@ServiceTest
@DisplayName("[TilService] updateTil")
class UpdateTilTest {
    @InjectMocks
    private TilService tilService;

    @Mock
    private TilRepository tilRepository;

    @Mock
    private TagRepository tagRepository;

    @Mock
    private TilTagRepository tilTagRepository;

    @Spy
    private ModelMapper modelMapper;

    @BeforeEach
    void beforeEach() {
        modelMapper.getConfiguration()
                .setFieldMatchingEnabled(true)
                .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE);
    }

    @Test
    @DisplayName("[Fail] til does not exist")
    void failIfTilDoesNotExist() {
        // Given
        final String profileUuid = UUID.randomUUID().toString();

        final String tilUuid = UUID.randomUUID().toString();

        doReturn(Optional.empty())
                .when(tilRepository)
                .findByUuid(tilUuid);

        final TilUpdateRequestDto requestDto = TilUpdateRequestDtoFactory.createDefaultTilUpdateRequestDto();

        // When, Then
        final TilException result = assertThrows(TilException.class, () -> tilService.updateTil(profileUuid, tilUuid, requestDto));

        assertThat(result.getStatus()).isEqualTo(TilStatus.NOT_EXISTING_TIL);
    }

    @Test
    @DisplayName("[Fail] member does not have authorization")
    void failIfMemberDoesNotHaveAuthorization() {
        // Given
        final Profile profile = ProfileFactory.createDefaultProfile();

        final String profileUuid = UUID.randomUUID().toString();

        final Til til = TilFactory.createDefaultTil(profile);

        doReturn(Optional.of(til))
                .when(tilRepository)
                .findByUuid(til.getUuid());

        final TilUpdateRequestDto requestDto = TilUpdateRequestDtoFactory.createDefaultTilUpdateRequestDto();

        // When, Then
        final TilException result = assertThrows(TilException.class, () -> tilService.updateTil(profileUuid, til.getUuid(), requestDto));

        assertThat(result.getStatus()).isEqualTo(TilStatus.NOT_AUTHORIZED_MEMBER);
    }

    @Test
    @DisplayName("[Success] til is acceptable")
    void successIfTilIsAcceptable() {
        // Given
        final Profile profile = ProfileFactory.createDefaultProfile();

        final Til til = TilFactory.createDefaultTil(profile);

        doReturn(Optional.of(til))
                .when(tilRepository)
                .findByUuid(til.getUuid());

        final List<String> tags = FactoryPreset.TAGS;

        final Tag tag1 = TagFactory.create(tags.get(0));

        doReturn(Optional.of(tag1))
                .when(tagRepository)
                .findByName(tag1.getName());

        final Tag tag2 = TagFactory.create(tags.get(1));

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

        final TilUpdateRequestDto requestDto = TilUpdateRequestDtoFactory.createDefaultTilUpdateRequestDto();

        // When
        final TilUpdateResponseDto result = tilService.updateTil(profile.getUuid(), til.getUuid(), requestDto);

        // Then
        assertThat(result.getTilUuid().length()).isEqualTo(36);
    }
}