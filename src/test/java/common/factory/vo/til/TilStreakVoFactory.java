package common.factory.vo.til;

import me.tiary.vo.til.TilStreakVo;

import java.time.LocalDate;

public final class TilStreakVoFactory {
    public static TilStreakVo createDefaultTilStreakVo() {
        return create(LocalDate.now(), 1);
    }

    public static TilStreakVo create(final LocalDate date, final int tilNumber) {
        return TilStreakVo.builder()
                .date(date)
                .tilNumber(tilNumber)
                .build();
    }
}