package me.tiary.dto.til;

import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
@RequiredArgsConstructor
@Builder
@Getter
public class TilReadResponseDto {
    private final String title;

    private final String content;

    private final String author;

    private final LocalDateTime createdDate;
}