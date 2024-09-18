package me.tiary.repository;

import me.tiary.domain.TilTag;
import me.tiary.domain.composite.TilTagId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TilTagRepository extends JpaRepository<TilTag, TilTagId> {
    @Query(value = "select tt from TilTag tt join fetch tt.til where tt.til.profile.nickname = :tilProfileNickname and tt.tag.name = :tagName",
            countQuery = "select count(tt) from TilTag tt where tt.til.profile.nickname = :tilProfileNickname and tt.tag.name = :tagName")
    Page<TilTag> findByTilProfileNicknameAndTagNameJoinFetchTil(@Param("tilProfileNickname") final String tilProfileNickname, @Param("tagName") final String tagName, final Pageable pageable);

    List<TilTag> findAllByTilUuid(final String tilUuid);

    @Query("select tt from TilTag tt join fetch tt.tag where tt.til.uuid = :tilUuid")
    List<TilTag> findAllByTilUuidJoinFetchTag(@Param("tilUuid") final String tilUuid);

    @Query("select tt from TilTag tt join fetch tt.tag where tt.til.profile.nickname = :tilProfileNickname")
    List<TilTag> findAllByTilProfileNicknameJoinFetchTag(@Param("tilProfileNickname") final String tilProfileNickname);

    void deleteAllByTilUuid(final String tilUuid);
}