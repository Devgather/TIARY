package factory.dto.account;

import me.tiary.dto.account.AccountCreationResponseDto;

public final class AccountCreationResponseDtoFactory {
    public static AccountCreationResponseDto createDefaultAccountCreationResponseDto() {
        return create("test@example.com");
    }

    public static AccountCreationResponseDto create(final String email) {
        return AccountCreationResponseDto.builder()
                .email(email)
                .build();
    }
}