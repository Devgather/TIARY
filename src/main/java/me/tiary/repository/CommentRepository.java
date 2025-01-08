package me.tiary.repository;

import me.tiary.domain.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Query("select c from Comment c join fetch c.profile where c.uuid = :uuid")
    Optional<Comment> findJoinFetchProfileByUuid(@Param("uuid") final String uuid);

    @Query(value = "select c from Comment c join fetch c.profile where c.til.uuid = :tilUuid",
            countQuery = "select count(c) from Comment c where c.til.uuid = :tilUuid")
    Page<Comment> findJoinFetchProfileByTilUuid(@Param("tilUuid") final String tilUuid, final Pageable pageable);
}