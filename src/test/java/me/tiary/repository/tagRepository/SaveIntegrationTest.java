package me.tiary.repository.tagRepository;

import common.annotation.repository.RepositoryIntegrationTest;
import common.factory.domain.TagFactory;
import me.tiary.domain.Tag;
import me.tiary.repository.TagRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@RepositoryIntegrationTest
@DisplayName("[TagRepository - Integration] save")
class SaveIntegrationTest {
    @Autowired
    private TagRepository tagRepository;

    @Test
    @DisplayName("[Fail] name is null")
    void failIfNameIsNull() {
        // Given
        final Tag tag = TagFactory.create(null);

        // When, Then
        assertThrows(DataIntegrityViolationException.class, () -> tagRepository.save(tag));
    }

    @Test
    @DisplayName("[Success] tag is acceptable")
    void explanation() {
        // Given
        final Tag tag = TagFactory.createDefaultTag();

        // When
        final Tag result = tagRepository.save(tag);

        // Then
        assertThat(result.getId()).isNotNull();
        assertThat(result.getName()).isEqualTo(tag.getName());
        assertThat(result.getUuid().length()).isEqualTo(36);
    }
}