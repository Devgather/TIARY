package me.tiary.profile.service;

import lombok.RequiredArgsConstructor;
import me.tiary.profile.repository.ProfileRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProfileService {

    private final ProfileRepository profileRepository;

    public boolean isNicknameAvailable(final String nickname) {
        return !profileRepository.existsByNickname(nickname);
    }

}
