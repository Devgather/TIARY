package me.tiary.repository;

import me.tiary.domain.OAuth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OAuthRepository extends JpaRepository<OAuth, Long> {
    Optional<OAuth> findByIdentifierAndProvider(final String identifier, final String provider);
}