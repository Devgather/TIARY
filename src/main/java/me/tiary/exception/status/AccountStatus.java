package me.tiary.exception.status;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum AccountStatus {
    UNREQUESTED_EMAIL_VERIFICATION(HttpStatus.BAD_REQUEST, "must request email verification"),
    UNVERIFIED_EMAIL(HttpStatus.BAD_REQUEST, "must verify email"),
    VERIFIED_EMAIL(HttpStatus.BAD_REQUEST, "must be an unverified email"),
    NOT_MATCHING_CODE(HttpStatus.BAD_REQUEST, "must provide matching code"),
    NOT_EXISTING_PROFILE_UUID(HttpStatus.NOT_FOUND, "must be an existing profile uuid"),
    EXISTING_EMAIL(HttpStatus.CONFLICT, "must not be an existing email"),
    EXISTING_ANOTHER_ACCOUNT_ON_PROFILE(HttpStatus.CONFLICT, "must register to profile that has not account");

    private final HttpStatus httpStatus;
    private final String message;
}