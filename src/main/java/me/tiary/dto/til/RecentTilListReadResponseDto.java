package me.tiary.dto.til;

import lombok.*;
import me.tiary.vo.til.TilWithProfileVo;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
@RequiredArgsConstructor
@Builder
@Getter
public class RecentTilListReadResponseDto {
    private final List<TilWithProfileVo> tils;
}