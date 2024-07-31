package me.tiary.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.tiary.exception.status.TagStatus;

@RequiredArgsConstructor
@Getter
public class TagException extends RuntimeException {
    private final TagStatus status;
}