package common.factory.dto.til;

import me.tiary.dto.til.TilEditResponseDto;

import java.util.UUID;

public final class TilEditResponseDtoFactory {
    public static TilEditResponseDto createDefaultTilEditResponseDto() {
        return create(UUID.randomUUID().toString());
    }

    public static TilEditResponseDto create(final String tilUuid) {
        return TilEditResponseDto.builder()
                .tilUuid(tilUuid)
                .build();
    }
}