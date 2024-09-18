package me.tiary.service.tilservice;

import common.annotation.service.ServiceTest;
import common.factory.domain.ProfileFactory;
import common.factory.domain.TilFactory;
import common.factory.dto.til.TilEditRequestDtoFactory;
import me.tiary.domain.Profile;
import me.tiary.domain.Til;
import me.tiary.dto.til.TilEditRequestDto;
import me.tiary.dto.til.TilEditResponseDto;
import me.tiary.exception.TilException;
import me.tiary.exception.status.TilStatus;
import me.tiary.repository.TilRepository;
import me.tiary.service.TilService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.modelmapper.ModelMapper;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;

@ServiceTest
@DisplayName("[TilService] updateTil")
class UpdateTilTest {
    @InjectMocks
    private TilService tilService;

    @Mock
    private TilRepository tilRepository;

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

        final TilEditRequestDto requestDto = TilEditRequestDtoFactory.createDefaultTilEditRequestDto();

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

        final String tilUuid = til.getUuid();

        doReturn(Optional.of(til))
                .when(tilRepository)
                .findByUuid(tilUuid);

        final TilEditRequestDto requestDto = TilEditRequestDtoFactory.createDefaultTilEditRequestDto();

        // When, Then
        final TilException result = assertThrows(TilException.class, () -> tilService.updateTil(profileUuid, tilUuid, requestDto));

        assertThat(result.getStatus()).isEqualTo(TilStatus.NOT_AUTHORIZED_MEMBER);
    }

    @Test
    @DisplayName("[Success] til is acceptable")
    void successIfTilIsAcceptable() {
        // Given
        final Profile profile = ProfileFactory.createDefaultProfile();

        final String profileUuid = profile.getUuid();

        final Til til = TilFactory.createDefaultTil(profile);

        final String tilUuid = til.getUuid();

        doReturn(Optional.of(til))
                .when(tilRepository)
                .findByUuid(tilUuid);

        final TilEditRequestDto requestDto = TilEditRequestDtoFactory.createDefaultTilEditRequestDto();

        // When
        final TilEditResponseDto result = tilService.updateTil(profileUuid, tilUuid, requestDto);

        // Then
        assertThat(result.getTilUuid()).hasSize(36);
    }
}