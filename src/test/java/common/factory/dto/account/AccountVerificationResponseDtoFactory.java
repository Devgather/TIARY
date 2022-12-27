package common.factory.dto.account;

import me.tiary.dto.account.AccountVerificationResponseDto;

import java.util.UUID;

public final class AccountVerificationResponseDtoFactory {
    public static AccountVerificationResponseDto createDefaultAccountVerificationResponseDto() {
        return create(UUID.randomUUID().toString());
    }

    public static AccountVerificationResponseDto create(final String uuid) {
        return AccountVerificationResponseDto.builder()
                .uuid(uuid)
                .build();
    }
}