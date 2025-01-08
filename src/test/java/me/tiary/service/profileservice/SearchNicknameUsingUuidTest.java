package me.tiary.service.profileservice;

import common.annotation.service.ServiceTest;
import common.factory.domain.ProfileFactory;
import me.tiary.domain.Profile;
import me.tiary.exception.ProfileException;
import me.tiary.exception.status.ProfileStatus;
import me.tiary.repository.ProfileRepository;
import me.tiary.service.ProfileService;
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
@DisplayName("[ProfileService] searchNicknameUsingUuid")
class SearchNicknameUsingUuidTest {
    @InjectMocks
    private ProfileService profileService;

    @Mock
    private ProfileRepository profileRepository;

    @Test
    @DisplayName("[Fail] profile does not exist")
    void failIfProfileDoesNotExist() {
        // Given
        doReturn(Optional.empty())
                .when(profileRepository)
                .findLeftJoinFetchAccountByUuid(any(String.class));

        final String uuid = UUID.randomUUID().toString();

        // When, Then
        final ProfileException result = assertThrows(ProfileException.class, () -> profileService.searchNicknameUsingUuid(uuid));

        assertThat(result.getStatus()).isEqualTo(ProfileStatus.NOT_EXISTING_PROFILE);
    }

    @Test
    @DisplayName("[Success] profile does exist")
    void successIfProfileDoesExist() {
        // Given
        final Profile profile = ProfileFactory.createDefaultProfile();

        doReturn(Optional.of(profile))
                .when(profileRepository)
                .findLeftJoinFetchAccountByUuid(profile.getUuid());

        // When
        final String result = profileService.searchNicknameUsingUuid(profile.getUuid());

        // Then
        assertThat(result).isEqualTo(profile.getNickname());
    }
}