package common.factory.dto.til;

import common.config.factory.FactoryPreset;
import me.tiary.dto.til.TilReadResponseDto;

import java.time.LocalDateTime;
import java.util.List;

public final class TilReadResponseDtoFactory {
    public static TilReadResponseDto createDefaultTilReadResponseDto() {
        return create(FactoryPreset.TITLE, FactoryPreset.CONTENT, FactoryPreset.MARKDOWN, FactoryPreset.TAGS, FactoryPreset.NICKNAME, LocalDateTime.now());
    }

    public static TilReadResponseDto create(final String title,
                                            final String content,
                                            final String markdown,
                                            final List<String> tags,
                                            final String author,
                                            final LocalDateTime createdDate) {
        return TilReadResponseDto.builder()
                .title(title)
                .content(content)
                .markdown(markdown)
                .tags(tags)
                .author(author)
                .createdDate(createdDate)
                .build();
    }
}