package me.tiary.service;

import lombok.RequiredArgsConstructor;
import me.tiary.domain.Profile;
import me.tiary.dto.profile.*;
import me.tiary.exception.ProfileException;
import me.tiary.exception.status.ProfileStatus;
import me.tiary.properties.aws.AwsStorageProperties;
import me.tiary.repository.ProfileRepository;
import me.tiary.utility.aws.AwsS3Manager;
import me.tiary.utility.common.FileUtility;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProfileService {
    public static final String PROFILE_PICTURE_PATH = "/profile/picture";

    private final ProfileRepository profileRepository;

    private final AwsS3Manager awsS3Manager;

    private final AwsStorageProperties awsStorageProperties;

    private final ModelMapper modelMapper;

    public boolean checkNicknameExistence(final String nickname) {
        return profileRepository.findByNickname(nickname).isPresent();
    }

    public String searchNicknameUsingUuid(final String uuid) {
        final Profile profile = profileRepository.findByUuidLeftJoinFetchAccount(uuid)
                .orElseThrow(() -> new ProfileException(ProfileStatus.NOT_EXISTING_PROFILE));

        return profile.getNickname();
    }

    @Transactional
    public ProfileCreationResponseDto createProfile(final ProfileCreationRequestDto requestDto) {
        final String nickname = requestDto.getNickname();

        if (checkNicknameExistence(nickname)) {
            throw new ProfileException(ProfileStatus.EXISTING_NICKNAME);
        }

        String storageUrl = awsStorageProperties.getUrl();

        if (storageUrl.endsWith("/")) {
            storageUrl = storageUrl.substring(0, storageUrl.length() - 1);
        }

        final Profile profile = Profile.builder()
                .nickname(nickname)
                .picture(storageUrl + Profile.BASIC_PICTURE)
                .build();

        final Profile result = profileRepository.save(profile);

        return modelMapper.map(result, ProfileCreationResponseDto.class);
    }

    public ProfileReadResponseDto readProfile(final String nickname) {
        final Profile result = profileRepository.findByNickname(nickname)
                .orElseThrow(() -> new ProfileException(ProfileStatus.NOT_EXISTING_PROFILE));

        return modelMapper.map(result, ProfileReadResponseDto.class);
    }

    @Transactional
    public ProfilePictureUploadResponseDto uploadPicture(final String profileUuid, final ProfilePictureUploadRequestDto requestDto) {
        final Profile profile = profileRepository.findByUuid(profileUuid)
                .orElseThrow(() -> new ProfileException(ProfileStatus.NOT_EXISTING_PROFILE));

        final MultipartFile pictureFile = requestDto.getPictureFile();

        final String contentType = pictureFile.getContentType();

        if (contentType == null) {
            throw new ProfileException(ProfileStatus.NOT_EXISTING_CONTENT_TYPE);
        }

        if (!contentType.matches("(^image)(/)\\w*")) {
            throw new ProfileException(ProfileStatus.NOT_SUPPORTING_CONTENT_TYPE);
        }

        final Function<String, String> titleGenerator = createPictureTitleGenerator(profileUuid);

        final String picturePath = awsS3Manager.uploadFiles(titleGenerator, List.of(pictureFile)).get(0);

        String storageUrl = awsStorageProperties.getUrl();

        if (!storageUrl.endsWith("/")) {
            storageUrl += '/';
        }

        profile.updatePicture(storageUrl + picturePath);

        return modelMapper.map(profile, ProfilePictureUploadResponseDto.class);
    }

    @Transactional
    public ProfileUpdateResponseDto updateProfile(final String profileUuid, final ProfileUpdateRequestDto requestDto) {
        final Profile profile = profileRepository.findByUuid(profileUuid)
                .orElseThrow(() -> new ProfileException(ProfileStatus.NOT_EXISTING_PROFILE));

        final String nickname = requestDto.getNickname();

        if (checkNicknameExistence(nickname)) {
            throw new ProfileException(ProfileStatus.EXISTING_NICKNAME);
        }

        profile.updateNickname(nickname);

        return modelMapper.map(profile, ProfileUpdateResponseDto.class);
    }

    public static Function<String, String> createPictureTitleGenerator(final String profileUuid) {
        return (originalFileName) -> profileUuid + PROFILE_PICTURE_PATH + FileUtility.getFileExtension(originalFileName);
    }
}
