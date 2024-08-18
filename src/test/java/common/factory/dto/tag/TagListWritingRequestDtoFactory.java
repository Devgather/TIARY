package common.factory.dto.tag;

import common.config.factory.FactoryPreset;
import me.tiary.dto.tag.TagListWritingRequestDto;

import java.util.List;

public final class TagListWritingRequestDtoFactory {
    public static TagListWritingRequestDto createDefaultTagListWritingRequestDto() {
        return create(FactoryPreset.TAGS);
    }

    public static TagListWritingRequestDto create(final List<String> tags) {
        return TagListWritingRequestDto.builder()
                .tags(tags)
                .build();
    }
}