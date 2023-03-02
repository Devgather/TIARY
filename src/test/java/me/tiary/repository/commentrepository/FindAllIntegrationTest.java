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
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RepositoryIntegrationTest
@DisplayName("[CommentRepository - Integration] findAll")
class FindAllIntegrationTest {
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
    @DisplayName("[Success] comments do not exist")
    void successIfCommentsDoNotExist() {
        // When
        final List<Comment> result = commentRepository.findAll();

        // Then
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("[Success] comments do exist")
    void successIfCommentsDoExist() {
        // Given
        final List<Comment> comments = new ArrayList<>();

        for (int index = 0; index < 10; index++) {
            comments.add(CommentFactory.createDefaultComment(profile, til));
        }

        commentRepository.saveAll(comments);

        JpaUtility.flushAndClear(em);

        // When
        final List<Comment> result = commentRepository.findAll();

        // Then
        assertThat(result).hasSize(10);
    }
}