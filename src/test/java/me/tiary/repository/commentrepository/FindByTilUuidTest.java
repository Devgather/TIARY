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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

@RepositoryIntegrationTest
@DisplayName("[CommentRepository] findByTilUuid")
class FindByTilUuidTest {
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
        // Given
        final Pageable pageable = PageRequest.of(0, 5, Sort.by("createdDate").descending());

        // When
        final Page<Comment> result = commentRepository.findByTilUuid(til.getUuid(), pageable);

        // Then
        assertThat(result.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("[Success] comment number does not meet request")
    void successIfCommentCountIs3() {
        // Given
        final ArrayList<Comment> comments = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            comments.add(CommentFactory.createDefaultComment(profile, til));
        }

        commentRepository.saveAll(comments);

        JpaUtility.flushAndClear(em);

        final Pageable pageable = PageRequest.of(0, 5, Sort.by("createdDate").descending());

        // When
        final Page<Comment> result = commentRepository.findByTilUuid(til.getUuid(), pageable);

        // Then
        assertThat(result.getTotalPages()).isEqualTo(1);
        assertThat(result.getContent().size()).isEqualTo(3);
    }

    @Test
    @DisplayName("[Success] comment number does meet request")
    void successIfCommentCountIs13() {
        // Given
        final ArrayList<Comment> comments = new ArrayList<>();

        for (int i = 0; i < 13; i++) {
            comments.add(CommentFactory.createDefaultComment(profile, til));
        }

        commentRepository.saveAll(comments);

        JpaUtility.flushAndClear(em);

        final Pageable pageable1 = PageRequest.of(0, 5, Sort.by("createdDate").descending());
        final Pageable pageable2 = PageRequest.of(1, 5, Sort.by("createdDate").descending());
        final Pageable pageable3 = PageRequest.of(2, 5, Sort.by("createdDate").descending());

        // When
        final Page<Comment> result1 = commentRepository.findByTilUuid(til.getUuid(), pageable1);
        final Page<Comment> result2 = commentRepository.findByTilUuid(til.getUuid(), pageable2);
        final Page<Comment> result3 = commentRepository.findByTilUuid(til.getUuid(), pageable3);

        // Then
        assertThat(result1.getTotalPages()).isEqualTo(3);
        assertThat(result2.getTotalPages()).isEqualTo(3);
        assertThat(result3.getTotalPages()).isEqualTo(3);
        assertThat(result1.getContent().size()).isEqualTo(5);
        assertThat(result2.getContent().size()).isEqualTo(5);
        assertThat(result3.getContent().size()).isEqualTo(3);
    }
}