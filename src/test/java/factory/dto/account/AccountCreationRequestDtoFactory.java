package factory.dto.account;

import me.tiary.dto.account.AccountCreationRequestDto;

public final class AccountCreationRequestDtoFactory {
    public static AccountCreationRequestDto createDefaultAccountCreationRequestDto(final String profileUuid) {
        return create(profileUuid, "test@example.com", "test");
    }

    public static AccountCreationRequestDto create(final String profileUuid, final String email, final String password) {
        return AccountCreationRequestDto.builder()
                .profileUuid(profileUuid)
                .email(email)
                .password(password)
                .build();
    }
}