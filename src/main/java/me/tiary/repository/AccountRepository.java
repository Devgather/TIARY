package me.tiary.repository;

import me.tiary.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByProfileId(final Long profileId);

    Optional<Account> findByEmail(final String email);
}