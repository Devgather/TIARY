package common.factory.dto.til;


import common.factory.vo.til.TilVoFactory;
import me.tiary.dto.til.RecentTilListReadResponseDto;
import me.tiary.vo.til.TilVo;

import java.util.List;

public final class RecentTilListReadResponseDtoFactory {
    public static RecentTilListReadResponseDto createDefaultRecentTilListReadResponseDto() {
        return create(List.of(TilVoFactory.createDefaultTilVo()));
    }

    public static RecentTilListReadResponseDto create(final List<TilVo> tils) {
        return RecentTilListReadResponseDto.builder()
                .tils(tils)
                .build();
    }
}