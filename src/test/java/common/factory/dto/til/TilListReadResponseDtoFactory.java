package common.factory.dto.til;

import common.factory.vo.til.TilVoFactory;
import me.tiary.dto.til.TilListReadResponseDto;
import me.tiary.vo.til.TilVo;

import java.util.List;

public final class TilListReadResponseDtoFactory {
    public static TilListReadResponseDto createDefaultTilListReadResponseDto() {
        return create(List.of(TilVoFactory.createDefaultTilVo()));
    }

    public static TilListReadResponseDto create(final List<TilVo> tils) {
        return TilListReadResponseDto.builder()
                .tils(tils)
                .build();
    }
}