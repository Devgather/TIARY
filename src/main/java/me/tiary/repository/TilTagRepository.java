package me.tiary.repository;

import me.tiary.domain.TilTag;
import me.tiary.domain.composite.TilTagId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TilTagRepository extends JpaRepository<TilTag, TilTagId> {
    List<TilTag> findAllByTilUuid(final String tilUuid);

    @Query("select tt from TilTag tt join fetch tt.tag where tt.til.uuid = :tilUuid")
    List<TilTag> findAllByTilUuidJoinFetchTag(@Param("tilUuid") final String tilUuid);

    void deleteAllByTilUuid(final String tilUuid);
}