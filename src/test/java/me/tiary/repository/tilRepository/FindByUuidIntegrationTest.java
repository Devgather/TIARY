package me.tiary.repository.tilRepository;

import common.annotation.repository.RepositoryIntegrationTest;
import common.factory.domain.ProfileFactory;
import common.factory.domain.TilFactory;
import common.utility.JpaUtility;
import me.tiary.domain.Profile;
import me.tiary.domain.Til;
import me.tiary.repository.ProfileRepository;
import me.tiary.repository.TilRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@RepositoryIntegrationTest
@DisplayName("[TilRepository] findByUuid")
class FindByUuidIntegrationTest {
    @Autowired
    private TilRepository tilRepository;

    @Autowired
    private ProfileRepository profileRepository;

    @PersistenceContext
    private EntityManager em;

    private Profile profile;

    @BeforeEach
    void beforeEach() {
        profile = profileRepository.save(ProfileFactory.createDefaultProfile());

        JpaUtility.flushAndClear(em);
    }

    @Test
    @DisplayName("[Success] uuid does not exist")
    void successIfUuidDoesNotExist() {
        // When
        final Optional<Til> result = tilRepository.findByUuid(UUID.randomUUID().toString());

        // Then
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("[Success] uuid does exist")
    void successIfUuidDoesExist() {
        // Given
        final Til til = TilFactory.createDefaultTil(profile);

        tilRepository.save(til);

        JpaUtility.flushAndClear(em);

        // When
        final Optional<Til> result = tilRepository.findByUuid(til.getUuid());

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().getProfile()).isEqualTo(til.getProfile());
        assertThat(result.get().getTitle()).isEqualTo(til.getTitle());
        assertThat(result.get().getContent()).isEqualTo(til.getContent());
    }
}
