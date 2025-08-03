package me.tiary.profile.repository;

import me.tiary.profile.domain.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long> {

    boolean existsByNickname(String nickname);

}
