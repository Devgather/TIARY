package common.factory.dto.til;

import common.config.factory.FactoryPreset;
import me.tiary.dto.til.TilReadResponseDto;

import java.time.LocalDateTime;

public final class TilReadResponseDtoFactory {
    public static TilReadResponseDto createDefaultTilReadResponseDto() {
        return create(FactoryPreset.TITLE, FactoryPreset.CONTENT, FactoryPreset.NICKNAME, LocalDateTime.now());
    }

    public static TilReadResponseDto create(final String title,
                                            final String content,
                                            final String author,
                                            final LocalDateTime createdDate) {
        return TilReadResponseDto.builder()
                .title(title)
                .content(content)
                .author(author)
                .createdDate(createdDate)
                .build();
    }
}