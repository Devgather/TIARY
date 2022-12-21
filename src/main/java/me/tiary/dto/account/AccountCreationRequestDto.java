package me.tiary.dto.account;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
@RequiredArgsConstructor
@Builder
@Getter
@EqualsAndHashCode
public class AccountCreationRequestDto {
    @NotBlank
    private final String verificationUuid;

    @NotBlank
    private final String profileUuid;

    @NotBlank
    @Email
    private final String email;

    @NotBlank
    private final String password;
}