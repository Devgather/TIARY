package me.tiary.repository.profilerepository;

import common.annotation.repository.RepositoryIntegrationTest;
import common.factory.domain.ProfileFactory;
import common.utility.JpaUtility;
import me.tiary.domain.Profile;
import me.tiary.repository.ProfileRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@RepositoryIntegrationTest
@DisplayName("[ProfileRepository - Integration] findByUuid")
class FindByUuidIntegrationTest {
    @Autowired
    private ProfileRepository profileRepository;

    @PersistenceContext
    private EntityManager em;

    @Test
    @DisplayName("[Success] uuid does not exist")
    void successIfUuidDoesNotExist() {
        // When
        final Optional<Profile> result = profileRepository.findByUuid(UUID.randomUUID().toString());

        // Then
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("[Success] uuid does exist")
    void successIfUuidDoesExist() {
        // Given
        final Profile profile = ProfileFactory.createDefaultProfile();

        profileRepository.save(profile);

        JpaUtility.flushAndClear(em);

        // When
        final Optional<Profile> result = profileRepository.findByUuid(profile.getUuid());

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().getNickname()).isEqualTo(profile.getNickname());
        assertThat(result.get().getPicture()).isEqualTo(profile.getPicture());
    }
}