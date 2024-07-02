package me.tiary.dto.til;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
@RequiredArgsConstructor
@Builder
@Getter
public class TilReadResponseDto {
    private final String title;

    private final String content;

    private final String markdown;

    private final List<String> tags;

    private final String author;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private final LocalDateTime createdDate;
}