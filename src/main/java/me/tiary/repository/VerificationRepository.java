package me.tiary.repository;

import me.tiary.domain.Verification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface VerificationRepository extends JpaRepository<Verification, Long> {
    Optional<Verification> findByEmail(final String email);

    Optional<Verification> findByUuidAndEmail(final String uuid, final String email);

    @Query("delete from Verification v where v.lastModifiedDate <= :dateTime")
    @Modifying(flushAutomatically = true, clearAutomatically = true)
    void deleteByLastModifiedDateLessThanEqual(@Param("dateTime") final LocalDateTime dateTime);
}