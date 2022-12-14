package common.factory.dto.account;

import common.factory.utility.jwt.JwtProviderFactory;
import me.tiary.dto.account.AccountLoginResponseDto;
import me.tiary.utility.jwt.JwtProvider;

import java.util.HashMap;
import java.util.Map;

public final class AccountLoginResponseDtoFactory {
    public static AccountLoginResponseDto createDefaultAccountLoginResponseDto() {
        final Map<String, String> payload = new HashMap<>();
        payload.put("data", "Test");

        final String accessToken = JwtProviderFactory.createAccessTokenProvider().generate(payload);

        final JwtProvider refreshTokenProvider = JwtProviderFactory.createRefreshTokenProvider();

        final String refreshToken = refreshTokenProvider.generate(payload);

        final int refreshTokenValidSeconds = refreshTokenProvider.getValidSeconds();

        return create(accessToken, refreshToken, refreshTokenValidSeconds);
    }

    public static AccountLoginResponseDto create(final String accessToken,
                                                 final String refreshToken,
                                                 final int refreshTokenValidSeconds) {
        return AccountLoginResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .refreshTokenValidSeconds(refreshTokenValidSeconds)
                .build();
    }
}
