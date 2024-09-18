package common.factory.dto.profile;

import common.config.factory.FactoryPreset;
import me.tiary.dto.profile.ProfileUpdateResponseDto;

public class ProfileUpdateResponseDtoFactory {
    public static ProfileUpdateResponseDto createDefaultProfileUpdateResponseDto() {
        return create(FactoryPreset.NICKNAME);
    }

    public static ProfileUpdateResponseDto create(final String nickname) {
        return ProfileUpdateResponseDto.builder()
                .nickname(nickname)
                .build();
    }
}