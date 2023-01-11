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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RepositoryIntegrationTest
@DisplayName("[CommentRepository] findAllByTilUuid")
class FindAllByTilUuidTest {
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
    @DisplayName("[Success] comment does not exist")
    void successIfCommentDoesNotExist() {
        // When
        final List<Comment> result = commentRepository.findAllByTilUuid(til.getUuid());

        // Then
        assertThat(result.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("[Success] comment does exist")
    void successIfCommentDoesExist() {
        // Given
        final Comment comment1 = CommentFactory.createDefaultComment(profile, til);
        final Comment comment2 = CommentFactory.createDefaultComment(profile, til);

        commentRepository.save(comment1);
        commentRepository.save(comment2);

        JpaUtility.flushAndClear(em);

        // When
        final List<Comment> result = commentRepository.findAllByTilUuid(til.getUuid());

        // Then
        assertThat(result).isEqualTo(List.of(comment1, comment2));
    }
}
