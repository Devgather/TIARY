package common.factory.dto.til;

import common.factory.vo.til.TilVoFactory;
import me.tiary.dto.til.TilListReadResponseDto;
import me.tiary.vo.til.TilWithProfileVo;

import java.util.List;

public final class TilListReadResponseDtoFactory {
    public static TilListReadResponseDto createDefaultTilListReadResponseDto() {
        return create(List.of(TilVoFactory.createDefaultTilWithProfileVo()), 1);
    }

    public static TilListReadResponseDto create(final List<TilWithProfileVo> tils, final int totalPages) {
        return TilListReadResponseDto.builder()
                .tils(tils)
                .totalPages(totalPages)
                .build();
    }
}