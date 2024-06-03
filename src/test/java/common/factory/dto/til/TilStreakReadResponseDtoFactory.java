package common.factory.dto.til;

import common.factory.vo.til.TilStreakVoFactory;
import me.tiary.dto.til.TilStreakReadResponseDto;
import me.tiary.vo.til.TilStreakVo;

import java.util.List;

public final class TilStreakReadResponseDtoFactory {
    public static TilStreakReadResponseDto createDefaultTilStreakReadResponseDto() {
        return create(List.of(TilStreakVoFactory.createDefaultTilStreakVo()));
    }

    public static TilStreakReadResponseDto create(final List<TilStreakVo> streaks) {
        return TilStreakReadResponseDto.builder()
                .streaks(streaks)
                .build();
    }
}