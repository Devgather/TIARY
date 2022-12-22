package me.tiary.dto.account;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
public class AccountLoginResultDto {
    private final String accessToken;
    private final String refreshToken;
}