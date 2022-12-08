package me.tiary.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.tiary.exception.status.AccountStatus;

@RequiredArgsConstructor
@Getter
public class AccountException extends RuntimeException {
    private final AccountStatus status;
}