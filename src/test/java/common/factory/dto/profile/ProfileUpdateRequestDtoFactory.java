package common.factory.dto.profile;

import me.tiary.dto.profile.ProfileUpdateRequestDto;

public final class ProfileUpdateRequestDtoFactory {
    public static ProfileUpdateRequestDto createDefaultProfileUpdateRequestDto() {
        return create("New");
    }

    public static ProfileUpdateRequestDto create(final String nickname) {
        return ProfileUpdateRequestDto.builder()
                .nickname(nickname)
                .build();
    }
}