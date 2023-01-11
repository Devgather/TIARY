package common.factory.dto.profile;

import common.config.factory.FactoryPreset;
import me.tiary.dto.profile.ProfileReadResponseDto;

public final class ProfileReadResponseDtoFactory {
    public static ProfileReadResponseDto createDefaultProfileReadResponseDto() {
        return create(FactoryPreset.NICKNAME, FactoryPreset.STORAGE + FactoryPreset.PICTURE);
    }

    public static ProfileReadResponseDto create(final String nickname, final String picture) {
        return ProfileReadResponseDto.builder()
                .nickname(nickname)
                .picture(picture)
                .build();
    }
}