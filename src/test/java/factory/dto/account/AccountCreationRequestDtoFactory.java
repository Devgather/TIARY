package factory.dto.account;

import config.factory.FactoryPreset;
import me.tiary.dto.account.AccountCreationRequestDto;

public final class AccountCreationRequestDtoFactory {
    public static AccountCreationRequestDto createDefaultAccountCreationRequestDto(final String verificationUuid,
                                                                                   final String profileUuid) {
        return create(verificationUuid, profileUuid, FactoryPreset.EMAIL, FactoryPreset.PASSWORD);
    }

    public static AccountCreationRequestDto create(final String verificationUuid,
                                                   final String profileUuid,
                                                   final String email,
                                                   final String password) {
        return AccountCreationRequestDto.builder()
                .verificationUuid(verificationUuid)
                .profileUuid(profileUuid)
                .email(email)
                .password(password)
                .build();
    }
}