package common.factory.dto.til;

import common.config.factory.FactoryPreset;
import me.tiary.dto.til.TilEditRequestDto;

import java.util.List;

public final class TilEditRequestDtoFactory {
    public static TilEditRequestDto createDefaultTilEditRequestDto() {
        return create(FactoryPreset.TITLE, FactoryPreset.CONTENT, FactoryPreset.TAGS);
    }

    public static TilEditRequestDto create(final String title, final String content, final List<String> tags) {
        return TilEditRequestDto.builder()
                .title(title)
                .content(content)
                .tags(tags)
                .build();
    }
}