package common.factory.dto.til;

import common.config.factory.FactoryPreset;
import me.tiary.dto.til.TilEditRequestDto;

public final class TilEditRequestDtoFactory {
    public static TilEditRequestDto createDefaultTilEditRequestDto() {
        return create(FactoryPreset.TITLE, FactoryPreset.CONTENT);
    }

    public static TilEditRequestDto create(final String title, final String content) {
        return TilEditRequestDto.builder()
                .title(title)
                .content(content)
                .build();
    }
}