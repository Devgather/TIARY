package common.factory.dto.til;

import common.factory.vo.til.TilWithProfileVoFactory;
import me.tiary.dto.til.RecentTilListReadResponseDto;
import me.tiary.vo.til.TilWithProfileVo;

import java.util.List;

public final class RecentTilListReadResponseDtoFactory {
    public static RecentTilListReadResponseDto createDefaultRecentTilListReadResponseDto() {
        return create(List.of(TilWithProfileVoFactory.createDefaultTilWithProfileVo()));
    }

    public static RecentTilListReadResponseDto create(final List<TilWithProfileVo> tils) {
        return RecentTilListReadResponseDto.builder()
                .tils(tils)
                .build();
    }
}