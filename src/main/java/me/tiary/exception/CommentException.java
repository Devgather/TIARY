package me.tiary.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.tiary.exception.status.CommentStatus;

@Getter
@RequiredArgsConstructor
public class CommentException extends RuntimeException {
    private final CommentStatus status;
}