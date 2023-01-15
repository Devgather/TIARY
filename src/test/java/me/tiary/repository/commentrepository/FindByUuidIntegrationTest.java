package me.tiary.repository.commentrepository;

import common.annotation.repository.RepositoryIntegrationTest;
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

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@RepositoryIntegrationTest
@DisplayName("[CommentRepository - Integration] findByUuid")
public class FindByUuidIntegrationTest {
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
    @DisplayName("[Success] uuid does not exist")
    void successIfUuidDoesNotExist() {
        // When
        final Optional<Comment> result = commentRepository.findByUuid(UUID.randomUUID().toString());

        // Then
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("[Success] uuid does exist")
    void successIfUuidDoesExist() {
        // Given
        final Comment comment = CommentFactory.createDefaultComment(profile, til);

        commentRepository.save(comment);

        JpaUtility.flushAndClear(em);

        // When
        final Optional<Comment> result = commentRepository.findByUuid(comment.getUuid());

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().getProfile()).isEqualTo(comment.getProfile());
        assertThat(result.get().getTil()).isEqualTo(comment.getTil());
        assertThat(result.get().getContent()).isEqualTo(comment.getContent());
    }
}
