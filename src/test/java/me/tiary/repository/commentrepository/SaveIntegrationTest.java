package me.tiary.repository.commentrepository;

import common.annotation.repository.RepositoryIntegrationTest;
import common.config.factory.FactoryPreset;
import common.factory.domain.CommentFactory;
import common.factory.domain.ProfileFactory;
import common.factory.domain.TilFactory;
import common.utility.JpaUtility;
import me.tiary.domain.Comment;
import me.tiary.domain.Profile;
import me.tiary.domain.Til;
import me.tiary.repository.CommentRepository;
import me.tiary.repository.ProfileRepository;
import me.tiary.repository.TilRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@RepositoryIntegrationTest
@DisplayName("[CommentRepository - Integration] save")
class SaveIntegrationTest {
    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private TilRepository tilRepository;

    @PersistenceContext
    private EntityManager em;

    private Profile profile;

    private Til til;

    @BeforeEach
    void beforeEach() {
        profile = profileRepository.save(ProfileFactory.createDefaultProfile());

        til = tilRepository.save(TilFactory.createDefaultTil(profile));

        JpaUtility.flushAndClear(em);
    }

    @Test
    @DisplayName("[Fail] profile is null")
    void failIfProfileIsNull() {
        // Given
        final Comment comment = CommentFactory.create(null, til, FactoryPreset.CONTENT);

        // When, Then
        assertThrows(DataIntegrityViolationException.class, () -> commentRepository.save(comment));
    }

    @Test
    @DisplayName("[Fail] til is null")
    void failIfTilIsNull() {
        // Given
        final Comment comment = CommentFactory.create(profile, null, FactoryPreset.CONTENT);

        // When, Then
        assertThrows(DataIntegrityViolationException.class, () -> commentRepository.save(comment));
    }

    @Test
    @DisplayName("[Fail] content is null")
    void failIfContentIsNull() {
        // Given
        final Comment comment = CommentFactory.create(profile, til, null);

        // When, Then
        assertThrows(DataIntegrityViolationException.class, () -> commentRepository.save(comment));
    }

    @Test
    @DisplayName("[Success] comment is acceptable")
    void successIfCommentIsAcceptable() {
        // Given
        final Comment comment = CommentFactory.createDefaultComment(profile, til);

        // When
        final Comment result = commentRepository.save(comment);

        // Then
        assertThat(result.getId()).isNotNull();
        assertThat(result.getProfile()).isEqualTo(comment.getProfile());
        assertThat(result.getTil()).isEqualTo(comment.getTil());
        assertThat(result.getUuid()).hasSize(36);
        assertThat(result.getContent()).isEqualTo(comment.getContent());
    }
}