package me.tiary.repository.profilerepository;

import common.annotation.repository.RepositoryIntegrationTest;
import common.config.factory.FactoryPreset;
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

import static org.assertj.core.api.Assertions.assertThat;

@RepositoryIntegrationTest
@DisplayName("[ProfileRepository - Integration] findByNickname")
class FindByNicknameIntegrationTest {
    @Autowired
    private ProfileRepository profileRepository;

    @PersistenceContext
    private EntityManager em;

    @Test
    @DisplayName("[Success] nickname does not exist")
    void successIfNicknameDoesNotExist() {
        // When
        final Optional<Profile> result = profileRepository.findByNickname(FactoryPreset.NICKNAME);

        // Then
        assertThat(result.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("[Success] nickname does exist")
    void successIfNicknameDoesExist() {
        // Given
        final Profile profile = ProfileFactory.createDefaultProfile();

        profileRepository.save(profile);

        JpaUtility.flushAndClear(em);

        // When
        final Optional<Profile> result = profileRepository.findByNickname(FactoryPreset.NICKNAME);

        // Then
        assertThat(result.isPresent()).isTrue();
        assertThat(result.get().getNickname()).isEqualTo(profile.getNickname());
        assertThat(result.get().getPicture()).isEqualTo(profile.getPicture());
    }
}