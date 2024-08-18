package common.factory.dto.til;

import common.config.factory.FactoryPreset;
import me.tiary.dto.til.TilReadResponseDto;

import java.time.LocalDateTime;

public final class TilReadResponseDtoFactory {
    public static TilReadResponseDto createDefaultTilReadResponseDto() {
        return create(FactoryPreset.TITLE, FactoryPreset.CONTENT, FactoryPreset.MARKDOWN, FactoryPreset.NICKNAME, LocalDateTime.now());
    }

    public static TilReadResponseDto create(final String title,
                                            final String content,
                                            final String markdown,
                                            final String author,
                                            final LocalDateTime createdDate) {
        return TilReadResponseDto.builder()
                .title(title)
                .content(content)
                .markdown(markdown)
                .author(author)
                .createdDate(createdDate)
                .build();
    }
}