package factory.dto.account;

import me.tiary.dto.account.AccountLoginRequestDto;

public final class AccountLoginRequestDtoFactory {
    public static AccountLoginRequestDto createDefaultAccountLoginRequestDto() {
        return create("test@example.com", "test");
    }

    public static AccountLoginRequestDto create(final String email, final String password) {
        return AccountLoginRequestDto.builder()
                .email(email)
                .password(password)
                .build();
    }
}