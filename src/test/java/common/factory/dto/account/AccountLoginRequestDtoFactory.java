package common.factory.dto.account;

import common.config.factory.FactoryPreset;
import me.tiary.dto.account.AccountLoginRequestDto;

public final class AccountLoginRequestDtoFactory {
    public static AccountLoginRequestDto createDefaultAccountLoginRequestDto() {
        return create(FactoryPreset.EMAIL, FactoryPreset.PASSWORD);
    }

    public static AccountLoginRequestDto create(final String email, final String password) {
        return AccountLoginRequestDto.builder()
                .email(email)
                .password(password)
                .build();
    }
}