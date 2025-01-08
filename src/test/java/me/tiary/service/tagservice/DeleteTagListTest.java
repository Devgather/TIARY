package me.tiary.service.tagservice;

import common.annotation.service.ServiceTest;
import common.factory.domain.ProfileFactory;
import common.factory.domain.TilFactory;
import me.tiary.domain.Profile;
import me.tiary.domain.Til;
import me.tiary.exception.TagException;
import me.tiary.exception.status.TagStatus;
import me.tiary.repository.TilRepository;
import me.tiary.repository.TilTagRepository;
import me.tiary.service.TagService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;

@ServiceTest
@DisplayName("[TagService] deleteTagList")
class DeleteTagListTest {
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
        final String profileUuid = UUID.randomUUID().toString();

        final String tilUuid = UUID.randomUUID().toString();

        doReturn(Optional.empty())
                .when(tilRepository)
                .findJoinFetchProfileByUuid(tilUuid);

        // When, Then
        final TagException result = assertThrows(TagException.class, () -> tagService.deleteTagList(profileUuid, tilUuid));

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
                .findJoinFetchProfileByUuid(tilUuid);

        // When, Then
        final TagException result = assertThrows(TagException.class, () -> tagService.deleteTagList(profileUuid, tilUuid));

        assertThat(result.getStatus()).isEqualTo(TagStatus.NOT_AUTHORIZED_MEMBER);
    }

    @Test
    @DisplayName("[Success] tags are deleted")
    void successIfTagsAreDeleted() {
        // Given
        final Profile profile = ProfileFactory.createDefaultProfile();

        final String profileUuid = profile.getUuid();

        final Til til = TilFactory.createDefaultTil(profile);

        final String tilUuid = til.getUuid();

        doReturn(Optional.of(til))
                .when(tilRepository)
                .findJoinFetchProfileByUuid(tilUuid);

        doNothing()
                .when(tilTagRepository)
                .deleteAllByTilUuid(tilUuid);

        // When, Then
        assertDoesNotThrow(() -> tagService.deleteTagList(profileUuid, tilUuid));
    }
}