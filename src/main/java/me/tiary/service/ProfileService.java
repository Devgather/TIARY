package me.tiary.service;

import lombok.RequiredArgsConstructor;
import me.tiary.domain.Profile;
import me.tiary.dto.profile.ProfileCreationRequestDto;
import me.tiary.dto.profile.ProfileCreationResponseDto;
import me.tiary.dto.profile.ProfileReadResponseDto;
import me.tiary.exception.ProfileException;
import me.tiary.exception.status.ProfileStatus;
import me.tiary.repository.ProfileRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

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
    public ProfileCreationResponseDto createProfile(final ProfileCreationRequestDto requestDto) {
        final String nickname = requestDto.getNickname();

        if (checkNicknameDuplication(nickname)) {
            throw new ProfileException(ProfileStatus.EXISTING_NICKNAME);
        }

        final Profile profile = Profile.builder()
                .nickname(nickname)
                .picture(Profile.BASIC_PICTURE)
                .build();

        final Profile result = profileRepository.save(profile);

        return modelMapper.map(result, ProfileCreationResponseDto.class);
    }

    public ProfileReadResponseDto readProfile(final String nickname) {
        final Profile result = profileRepository.findByNickname(nickname)
                .orElseThrow(() -> new ProfileException(ProfileStatus.NOT_EXISTING_PROFILE));

        return modelMapper.map(result, ProfileReadResponseDto.class);
    }
}
