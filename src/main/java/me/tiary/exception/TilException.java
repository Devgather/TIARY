package me.tiary.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.tiary.exception.status.TilStatus;

@Getter
@RequiredArgsConstructor
public class TilException extends RuntimeException {
    private final TilStatus status;
}