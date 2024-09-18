package me.tiary.repository;

import me.tiary.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByEmail(final String email);

    @Query("select a from Account a join fetch a.profile where a.email = :email")
    Optional<Account> findByEmailJoinFetchProfile(@Param("email") final String email);
}