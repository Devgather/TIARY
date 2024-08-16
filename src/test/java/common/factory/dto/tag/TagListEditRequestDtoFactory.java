package common.factory.dto.tag;

import common.config.factory.FactoryPreset;
import me.tiary.dto.tag.TagListEditRequestDto;

import java.util.List;

public final class TagListEditRequestDtoFactory {
    public static TagListEditRequestDto createDefaultTagListEditRequestDto() {
        return create(FactoryPreset.TAGS);
    }

    public static TagListEditRequestDto create(final List<String> tags) {
        return TagListEditRequestDto.builder()
                .tags(tags)
                .build();
    }
}