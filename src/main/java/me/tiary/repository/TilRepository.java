package me.tiary.repository;

import me.tiary.domain.Til;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TilRepository extends JpaRepository<Til, Long> {
    Optional<Til> findByUuid(final String uuid);
}