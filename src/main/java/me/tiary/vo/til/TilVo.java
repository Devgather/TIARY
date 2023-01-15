package me.tiary.vo.til;

import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
@RequiredArgsConstructor
@Builder
@Getter
public class TilVo {
    private final String uuid;

    private final String title;

    private final String content;
}