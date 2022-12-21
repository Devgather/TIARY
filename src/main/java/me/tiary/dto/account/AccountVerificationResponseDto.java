package me.tiary.dto.account;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
@Getter
public class AccountVerificationResponseDto {
    private final String uuid;
}