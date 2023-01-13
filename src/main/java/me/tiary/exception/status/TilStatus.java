package me.tiary.exception.status;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum TilStatus {
    NOT_EXISTING_PROFILE(HttpStatus.NOT_FOUND, "must be an existing profile");

    private final HttpStatus httpStatus;
    private final String message;
}