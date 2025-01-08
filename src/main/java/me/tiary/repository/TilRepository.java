package me.tiary.repository;

import me.tiary.domain.Til;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TilRepository extends JpaRepository<Til, Long> {
    Optional<Til> findByUuid(final String uuid);

    Page<Til> findByProfileNickname(final String profileNickname, final Pageable pageable);

    @Query("select t from Til t join fetch t.profile where t.uuid = :uuid")
    Optional<Til> findJoinFetchProfileByUuid(@Param("uuid") final String uuid);

    List<Til> findAllByProfileNicknameAndCreatedDateBetween(final String profileNickname, final LocalDateTime startDate, final LocalDateTime endDate);

    void deleteByUuid(final String uuid);
}