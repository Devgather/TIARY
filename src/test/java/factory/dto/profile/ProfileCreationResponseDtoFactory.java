package factory.dto.profile;

import config.factory.FactoryPreset;
import me.tiary.dto.profile.ProfileCreationResponseDto;

import java.util.UUID;

public final class ProfileCreationResponseDtoFactory {
    public static ProfileCreationResponseDto createDefaultProfileCreationResponseDto() {
        return create(UUID.randomUUID().toString(), FactoryPreset.NICKNAME);
    }

    public static ProfileCreationResponseDto create(final String uuid, final String nickname) {
        return ProfileCreationResponseDto.builder()
                .uuid(uuid)
                .nickname(nickname)
                .build();
    }
}