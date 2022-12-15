package me.tiary.dto.account;

import lombok.*;

@Getter
@Builder
@RequiredArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
public class AccountLoginRequestDto {
    private final String email;
    private final String password;
}