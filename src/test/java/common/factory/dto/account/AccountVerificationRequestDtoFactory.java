package common.factory.dto.account;

import common.config.factory.FactoryPreset;
import me.tiary.dto.account.AccountVerificationRequestDto;

public final class AccountVerificationRequestDtoFactory {
    public static AccountVerificationRequestDto createDefaultAccountVerificationRequestDto(final String code) {
        return create(FactoryPreset.EMAIL, code);
    }

    public static AccountVerificationRequestDto create(final String email, final String code) {
        return AccountVerificationRequestDto.builder()
                .email(email)
                .code(code)
                .build();
    }
}