package common.factory.dto.til;

import me.tiary.dto.til.TilWritingResponseDto;

import java.util.UUID;

public final class TilWritingResponseDtoFactory {
    public static TilWritingResponseDto createDefaultTilWritingResponseDto() {
        return create(UUID.randomUUID().toString());
    }

    public static TilWritingResponseDto create(final String uuid) {
        return TilWritingResponseDto.builder()
                .uuid(uuid)
                .build();
    }
}