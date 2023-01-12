package common.factory.dto.til;

import common.config.factory.FactoryPreset;
import me.tiary.dto.til.TilWritingResponseDto;

import java.util.UUID;

public final class TilWritingResponseDtoFactory {
    public static TilWritingResponseDto createDefaultTilWritingResponseDto() {
        return create(UUID.randomUUID().toString(), FactoryPreset.TITLE, FactoryPreset.CONTENT);
    }

    public static TilWritingResponseDto create(final String uuid, final String title, final String content) {
        return TilWritingResponseDto.builder()
                .title(title)
                .content(content)
                .build();
    }
}