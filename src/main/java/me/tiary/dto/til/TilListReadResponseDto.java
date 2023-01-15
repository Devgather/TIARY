package me.tiary.dto.til;

import lombok.*;
import me.tiary.vo.til.TilWithProfileVo;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
@RequiredArgsConstructor
@Builder
@Getter
public class TilListReadResponseDto {
    private final List<TilWithProfileVo> tils;

    private final int totalPages;
}