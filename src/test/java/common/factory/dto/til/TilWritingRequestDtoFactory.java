package common.factory.dto.til;

import common.config.factory.FactoryPreset;
import me.tiary.dto.til.TilWritingRequestDto;

import java.util.List;

public final class TilWritingRequestDtoFactory {
    public static TilWritingRequestDto createDefaultWritingRequestDto() {
        return create(FactoryPreset.TITLE, FactoryPreset.CONTENT, FactoryPreset.TAGS);
    }

    public static TilWritingRequestDto create(final String title, final String content, List<String> tags) {
        return TilWritingRequestDto.builder()
                .title(title)
                .content(content)
                .tags(tags)
                .build();
    }
}