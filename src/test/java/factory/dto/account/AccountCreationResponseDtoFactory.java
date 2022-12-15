package factory.dto.account;

import config.factory.FactoryPreset;
import me.tiary.dto.account.AccountCreationResponseDto;

public final class AccountCreationResponseDtoFactory {
    public static AccountCreationResponseDto createDefaultAccountCreationResponseDto() {
        return create(FactoryPreset.EMAIL);
    }

    public static AccountCreationResponseDto create(final String email) {
        return AccountCreationResponseDto.builder()
                .email(email)
                .build();
    }
}