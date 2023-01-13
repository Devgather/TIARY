package me.tiary.service.tilservice;

import common.annotation.service.ServiceTest;
import common.config.factory.FactoryPreset;
import common.factory.domain.ProfileFactory;
import common.factory.domain.TagFactory;
import common.factory.domain.TilFactory;
import common.factory.domain.TilTagFactory;
import common.factory.dto.til.TilWritingRequestDtoFactory;
import me.tiary.domain.Profile;
import me.tiary.domain.Tag;
import me.tiary.domain.Til;
import me.tiary.domain.TilTag;
import me.tiary.dto.til.TilWritingRequestDto;
import me.tiary.dto.til.TilWritingResponseDto;
import me.tiary.exception.TilException;
import me.tiary.exception.status.TilStatus;
import me.tiary.repository.ProfileRepository;
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
import org.modelmapper.config.Configuration;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;

@ServiceTest
@DisplayName("[TilService] writeTil")
class WriteTilTest {
    @InjectMocks
    private TilService tilService;

    @Mock
    private TilRepository tilRepository;

    @Mock
    private TagRepository tagRepository;

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
                .setFieldAccessLevel(Configuration.AccessLevel.PRIVATE);
    }

    @Test
    @DisplayName("[Fail] profile does not exist")
    void failIfProfileDoesNotExist() {
        // Given
        final String profileUuid = UUID.randomUUID().toString();

        doReturn(Optional.empty())
                .when(profileRepository)
                .findByUuid(eq(profileUuid));

        final TilWritingRequestDto requestDto = TilWritingRequestDtoFactory.createDefaultWritingRequestDto();

        // When, Then
        final TilException result = assertThrows(TilException.class, () -> tilService.writeTil(profileUuid, requestDto));

        assertThat(result.getStatus()).isEqualTo(TilStatus.NOT_EXISTING_PROFILE);
    }

    @Test
    @DisplayName("[Success] til is acceptable")
    void successIfTilIsAcceptable() {
        // Given
        final String profileUuid = UUID.randomUUID().toString();

        final Profile profile = ProfileFactory.createDefaultProfile();

        final Til til = TilFactory.createDefaultTil(profile);

        final List<String> tags = FactoryPreset.TAGS;

        final Tag tag1 = TagFactory.create(tags.get(0));

        final Tag tag2 = TagFactory.create(tags.get(1));

        final List<TilTag> tilTags = List.of(TilTagFactory.create(til, tag1), TilTagFactory.create(til, tag2));

        doReturn(Optional.of(profile))
                .when(profileRepository)
                .findByUuid(any(String.class));

        doReturn(til)
                .when(tilRepository)
                .save(any(Til.class));

        doReturn(Optional.of(tag1))
                .when(tagRepository)
                .findByName(tag1.getName());

        doReturn(Optional.empty())
                .when(tagRepository)
                .findByName(tag2.getName());

        doReturn(tilTags)
                .when(tilTagRepository)
                .saveAll(any());

        final TilWritingRequestDto requestDto = TilWritingRequestDtoFactory.createDefaultWritingRequestDto();

        // When
        final TilWritingResponseDto result = tilService.writeTil(profileUuid, requestDto);

        // Then
        assertThat(result.getUuid().length()).isEqualTo(36);
    }
}