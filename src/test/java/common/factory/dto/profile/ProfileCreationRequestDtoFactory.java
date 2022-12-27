package common.factory.dto.profile;

import common.config.factory.FactoryPreset;
import me.tiary.dto.profile.ProfileCreationRequestDto;

public final class ProfileCreationRequestDtoFactory {
    public static ProfileCreationRequestDto createDefaultProfileCreationRequestDto() {
        return create(FactoryPreset.NICKNAME);
    }

    public static ProfileCreationRequestDto create(final String nickname) {
        return ProfileCreationRequestDto.builder()
                .nickname(nickname)
                .build();
    }
}