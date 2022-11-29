package me.tiary.exception.status;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ProfileStatus {
    EXISTING_NICKNAME(HttpStatus.CONFLICT, "must be a unique nickname");

    private final HttpStatus httpStatus;
    private final String message;
}
