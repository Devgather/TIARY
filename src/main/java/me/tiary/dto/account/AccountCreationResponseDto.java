package me.tiary.dto.account;

import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
@RequiredArgsConstructor
@Builder
@Getter
public class AccountCreationResponseDto {
    private final String email;
}