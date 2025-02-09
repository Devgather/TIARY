package me.tiary.service.tilservice;

import common.annotation.service.ServiceTest;
import common.factory.domain.ProfileFactory;
import common.factory.domain.TilFactory;
import common.factory.dto.til.TilWritingRequestDtoFactory;
import me.tiary.domain.Profile;
import me.tiary.domain.Til;
import me.tiary.dto.til.TilWritingRequestDto;
import me.tiary.dto.til.TilWritingResponseDto;
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
import org.modelmapper.config.Configuration;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

@ServiceTest
@DisplayName("[TilService] writeTil")
class WriteTilTest {
    @InjectMocks
    private TilService tilService;

    @Mock
    private TilRepository tilRepository;

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
                .findByUuid(profileUuid);

        final TilWritingRequestDto requestDto = TilWritingRequestDtoFactory.createDefaultWritingRequestDto();

        // When, Then
        final TilException result = assertThrows(TilException.class, () -> tilService.writeTil(profileUuid, requestDto));

        assertThat(result.getStatus()).isEqualTo(TilStatus.NOT_EXISTING_PROFILE);
    }

    @Test
    @DisplayName("[Success] til is acceptable")
    void successIfTilIsAcceptable() {
        // Given
        final Profile profile = ProfileFactory.createDefaultProfile();

        final String profileUuid = profile.getUuid();

        doReturn(Optional.of(profile))
                .when(profileRepository)
                .findByUuid(profileUuid);

        final Til til = TilFactory.createDefaultTil(profile);

        doReturn(til)
                .when(tilRepository)
                .save(any(Til.class));

        final TilWritingRequestDto requestDto = TilWritingRequestDtoFactory.createDefaultWritingRequestDto();

        // When
        final TilWritingResponseDto result = tilService.writeTil(profileUuid, requestDto);

        // Then
        assertThat(result.getUuid()).hasSize(36);
    }
}