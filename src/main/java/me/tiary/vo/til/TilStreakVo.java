package me.tiary.vo.til;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDate;

@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
@RequiredArgsConstructor
@Builder
@Getter
public class TilStreakVo {
    @JsonFormat(pattern = "yyyy-MM-dd")
    private final LocalDate date;

    private final int tilNumber;
}