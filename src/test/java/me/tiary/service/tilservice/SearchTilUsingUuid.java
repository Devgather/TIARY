package me.tiary.service.tilservice;

import common.annotation.service.ServiceTest;
import common.factory.domain.ProfileFactory;
import common.factory.domain.TilFactory;
import me.tiary.domain.Profile;
import me.tiary.domain.Til;
import me.tiary.exception.TilException;
import me.tiary.exception.status.TilStatus;
import me.tiary.repository.TilRepository;
import me.tiary.service.TilService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

@ServiceTest
@DisplayName("[TilService] searchTilUsingUuid")
public class SearchTilUsingUuid {
    @InjectMocks
    private TilService tilService;

    @Mock
    private TilRepository tilRepository;

    @Test
    @DisplayName("[Fail] til does not exist")
    void failIfTilDoesNotExist() {
        // Given
        doReturn(Optional.empty())
                .when(tilRepository)
                .findByUuid(any(String.class));

        // When, Then
        final TilException result = assertThrows(TilException.class, () -> tilService.searchTilUsingUuid(UUID.randomUUID().toString()));

        assertThat(result.getStatus()).isEqualTo(TilStatus.NOT_EXISTING_TIL);
    }

    @Test
    @DisplayName("[Success] til does exist")
    void successIfTilDoesExist() {
        // Given
        final Profile profile = ProfileFactory.createDefaultProfile();

        final Til til = TilFactory.createDefaultTil(profile);

        doReturn(Optional.of(til))
                .when(tilRepository)
                .findByUuid(til.getUuid());

        // When
        final Til result = tilService.searchTilUsingUuid(til.getUuid());

        // Then
        assertThat(result).isEqualTo(til);
    }
}