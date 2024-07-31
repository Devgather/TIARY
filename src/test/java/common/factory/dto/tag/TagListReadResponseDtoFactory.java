package common.factory.dto.tag;

import common.config.factory.FactoryPreset;
import me.tiary.dto.tag.TagListReadResponseDto;

import java.util.List;

public final class TagListReadResponseDtoFactory {
    public static TagListReadResponseDto createDefaultTagListReadResponseDto() {
        return create(FactoryPreset.TAGS);
    }

    public static TagListReadResponseDto create(final List<String> tags) {
        return TagListReadResponseDto.builder()
                .tags(tags)
                .build();
    }
}