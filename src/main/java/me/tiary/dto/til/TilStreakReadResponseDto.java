package me.tiary.dto.til;

import lombok.*;
import me.tiary.vo.til.TilStreakVo;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
@RequiredArgsConstructor
@Builder
@Getter
public class TilStreakReadResponseDto {
    private final List<TilStreakVo> streaks;
}