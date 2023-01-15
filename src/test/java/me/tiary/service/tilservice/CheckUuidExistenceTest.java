package me.tiary.service.tilservice;

import common.annotation.service.ServiceTest;
import common.factory.domain.ProfileFactory;
import common.factory.domain.TilFactory;
import me.tiary.domain.Til;
import me.tiary.repository.TilRepository;
import me.tiary.service.TilService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

@ServiceTest
@DisplayName("[TilService] checkUuidExistence")
class CheckUuidExistenceTest {
    @InjectMocks
    private TilService tilService;

    @Mock
    private TilRepository tilRepository;

    @Test
    @DisplayName("[Success] uuid does not exist")
    void successIfUuidDoesNotExist() {
        // Given
        doReturn(Optional.empty())
                .when(tilRepository)
                .findByUuid(any(String.class));

        // When
        final boolean result = tilService.checkUuidExistence(UUID.randomUUID().toString());

        // Then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("[Success] uuid does exist")
    void successIfUuidDoesExist() {
        // Given
        final Til til = TilFactory.createDefaultTil(ProfileFactory.createDefaultProfile());

        doReturn(Optional.of(til))
                .when(tilRepository)
                .findByUuid(til.getUuid());

        // When
        final boolean result = tilService.checkUuidExistence(til.getUuid());

        // Then
        assertThat(result).isTrue();
    }
}