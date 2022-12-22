package me.tiary.dto.account;

import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
@RequiredArgsConstructor
@Builder
@Getter
public class AccountVerificationResponseDto {
    private final String uuid;
}