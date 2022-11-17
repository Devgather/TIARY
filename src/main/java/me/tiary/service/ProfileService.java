package me.tiary.service;

import lombok.RequiredArgsConstructor;
import me.tiary.repository.ProfileRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProfileService {
    private final ProfileRepository profileRepository;

    public boolean checkNicknameDuplication(final String nickname) {
        return profileRepository.findByNickname(nickname).isPresent();
    }
}
