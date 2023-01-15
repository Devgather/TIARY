package common.factory.dto.til;

import common.config.factory.FactoryPreset;
import me.tiary.dto.til.TilUpdateRequestDto;

import java.util.List;

public final class TilUpdateRequestDtoFactory {
    public static TilUpdateRequestDto createDefaultTilUpdateRequestDto() {
        return create(FactoryPreset.TITLE, FactoryPreset.CONTENT, FactoryPreset.TAGS);
    }

    public static TilUpdateRequestDto create(final String title, final String content, final List<String> tags) {
        return TilUpdateRequestDto.builder()
                .title(title)
                .content(content)
                .tags(tags)
                .build();
    }
}