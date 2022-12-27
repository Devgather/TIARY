package me.tiary.repository;

import me.tiary.domain.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long> {
    Optional<Profile> findByUuid(final String uuid);

    @Query("select p from Profile p left join fetch p.account where p.uuid = :uuid")
    Optional<Profile> findByUuidLeftJoinFetchAccount(@Param("uuid") final String uuid);

    Optional<Profile> findByNickname(final String nickname);
}