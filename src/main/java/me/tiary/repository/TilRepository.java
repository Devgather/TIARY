package me.tiary.repository;

import me.tiary.domain.Til;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TilRepository extends JpaRepository<Til, Long> {
    Optional<Til> findByUuid(final String uuid);

    Page<Til> findByProfileNickname(final String profileNickname, final Pageable pageable);

    @Query("select t from Til t join fetch t.profile where t.uuid = :uuid")
    Optional<Til> findByUuidJoinFetchProfile(@Param("uuid") final String uuid);
}