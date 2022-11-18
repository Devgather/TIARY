package me.tiary.service;

import lombok.RequiredArgsConstructor;
import me.tiary.domain.Profile;
import me.tiary.dto.profile.ProfileCreationRequestDto;
import me.tiary.dto.profile.ProfileCreationResponseDto;
import me.tiary.exception.ProfileException;
import me.tiary.exception.status.ProfileStatus;
import me.tiary.repository.ProfileRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProfileService {
    private final ProfileRepository profileRepository;

    private final ModelMapper modelMapper;

    public boolean checkNicknameDuplication(final String nickname) {
        return profileRepository.findByNickname(nickname).isPresent();
    }

    @Transactional
    public ProfileCreationResponseDto createProfile(final ProfileCreationRequestDto dto) {
        final String nickname = dto.getNickname();

        if (checkNicknameDuplication(nickname)) {
            throw new ProfileException(ProfileStatus.EXISTING_NICKNAME);
        }

        final Profile profile = modelMapper.map(dto, Profile.class);

        final Profile result = profileRepository.save(profile);

        return modelMapper.map(result, ProfileCreationResponseDto.class);
    }
}
