package common.factory.dto.til;

import me.tiary.dto.til.TilDeletionResponseDto;

import java.util.UUID;

public final class TilDeletionResponseDtoFactory {
    public static TilDeletionResponseDto createDefaultTilDeletionResponseDto() {
        return create(UUID.randomUUID().toString());
    }

    public static TilDeletionResponseDto create(final String uuid) {
        return TilDeletionResponseDto.builder()
                .uuid(uuid)
                .build();
    }
}