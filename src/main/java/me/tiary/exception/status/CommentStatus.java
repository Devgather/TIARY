package me.tiary.exception.status;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum CommentStatus {
    NOT_AUTHORIZED_MEMBER(HttpStatus.FORBIDDEN, "muse be an authorized member"),
    NOT_EXISTING_COMMENT(HttpStatus.NOT_FOUND, "must be an existing comment"),
    NOT_EXISTING_PROFILE(HttpStatus.NOT_FOUND, "must be an existing profile"),
    NOT_EXISTING_TIL(HttpStatus.NOT_FOUND, "must be an existing til");

    private final HttpStatus httpStatus;
    private final String message;
}