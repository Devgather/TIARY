package me.tiary.repository.tagrepository;

import common.annotation.repository.RepositoryIntegrationTest;
import common.config.factory.FactoryPreset;
import common.factory.domain.TagFactory;
import common.utility.JpaUtility;
import me.tiary.domain.Tag;
import me.tiary.repository.TagRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RepositoryIntegrationTest
@DisplayName("[TagRepository - Integration] findByName")
class FindByNameIntegrationTest {
    @Autowired
    private TagRepository tagRepository;

    @PersistenceContext
    private EntityManager em;

    @Test
    @DisplayName("[Success] name does not exist")
    void successIfNameDoesNotExist() {
        // When
        final Optional<Tag> result = tagRepository.findByName(FactoryPreset.TAG);

        // Then
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("[Success] name does exist")
    void successIfNameDoesExist() {
        // Given
        final Tag tag = TagFactory.createDefaultTag();

        tagRepository.save(tag);

        JpaUtility.flushAndClear(em);

        // When
        final Optional<Tag> result = tagRepository.findByName(tag.getName());

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo(tag.getName());
    }
}
