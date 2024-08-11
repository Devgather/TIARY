package common.factory.dto.til;

import common.config.factory.FactoryPreset;
import me.tiary.dto.til.TilWritingRequestDto;

public final class TilWritingRequestDtoFactory {
    public static TilWritingRequestDto createDefaultWritingRequestDto() {
        return create(FactoryPreset.TITLE, FactoryPreset.CONTENT);
    }

    public static TilWritingRequestDto create(final String title, final String content) {
        return TilWritingRequestDto.builder()
                .title(title)
                .content(content)
                .build();
    }
}