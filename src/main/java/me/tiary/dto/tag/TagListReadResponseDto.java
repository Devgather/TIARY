package me.tiary.dto.tag;

import lombok.*;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
@RequiredArgsConstructor
@Builder
@Getter
public class TagListReadResponseDto {
    private final List<String> tags;
}