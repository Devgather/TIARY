package me.tiary.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.tiary.exception.status.ProfileStatus;

@Getter
@RequiredArgsConstructor
public class ProfileException extends RuntimeException {
    private final ProfileStatus status;
}