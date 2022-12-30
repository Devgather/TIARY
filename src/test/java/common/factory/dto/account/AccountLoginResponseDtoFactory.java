package common.factory.dto.account;

import common.factory.utility.jwt.JwtProviderFactory;
import me.tiary.dto.account.AccountLoginResponseDto;

import java.util.HashMap;
import java.util.Map;

public final class AccountLoginResponseDtoFactory {
    public static AccountLoginResponseDto createDefaultAccountLoginResponseDto() {
        final Map<String, String> payload = new HashMap<>();
        payload.put("data", "Test");

        final String accessToken = JwtProviderFactory.createAccessTokenProvider().generate(payload);

        final String refreshToken = JwtProviderFactory.createRefreshTokenProvider().generate(payload);

        return create(accessToken, refreshToken);
    }

    public static AccountLoginResponseDto create(final String accessToken, final String refreshToken) {
        return AccountLoginResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}
