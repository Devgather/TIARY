package me.tiary.service.tilservice;

import common.annotation.service.ServiceTest;
import common.factory.domain.ProfileFactory;
import common.factory.domain.TilFactory;
import me.tiary.domain.Profile;
import me.tiary.domain.Til;
import me.tiary.dto.til.TilDeletionResponseDto;
import me.tiary.exception.TilException;
import me.tiary.exception.status.TilStatus;
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

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;

@ServiceTest
@DisplayName("[TilService] deleteTil")
public class DeleteTilTest {
    @InjectMocks
    private TilService tilService;

    @Mock
    private TilRepository tilRepository;

    @Mock
    private TilTagRepository tilTagRepository;

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
    @DisplayName("[Fail] til does not exist")
    void failIfTilDoesNotExist() {
        // Given
        final String tilUuid = UUID.randomUUID().toString();

        doReturn(Optional.empty())
                .when(tilRepository)
                .findByUuid(tilUuid);

        final String profileUuid = UUID.randomUUID().toString();

        // When, Then
        final TilException result = assertThrows(TilException.class, () -> tilService.deleteTil(profileUuid, tilUuid));

        assertThat(result.getStatus()).isEqualTo(TilStatus.NOT_EXISTING_TIL);
    }

    @Test
    @DisplayName("[Fail] member does not have authorization")
    void failIfMemberDoesNotHaveAuthorization() {
        // Given
        final Til til = TilFactory.createDefaultTil(profile);

        doReturn(Optional.of(til))
                .when(tilRepository)
                .findByUuid(til.getUuid());

        final String profileUuid = UUID.randomUUID().toString();

        // When, Then
        final TilException result = assertThrows(TilException.class, () -> tilService.deleteTil(profileUuid, til.getUuid()));

        assertThat(result.getStatus()).isEqualTo(TilStatus.NOT_AUTHORIZED_MEMBER);
    }

    @Test
    @DisplayName("[Success] til is deleted")
    void successIfTilIsDeleted() {
        // Given
        final Til til = TilFactory.createDefaultTil(profile);

        doReturn(Optional.of(til))
                .when(tilRepository)
                .findByUuid(til.getUuid());

        // When
        final TilDeletionResponseDto result = tilService.deleteTil(profile.getUuid(), til.getUuid());

        // Then
        assertThat(result.getUuid()).hasSize(36);
    }
}