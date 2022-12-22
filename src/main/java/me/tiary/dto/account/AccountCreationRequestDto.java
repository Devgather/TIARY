package me.tiary.dto.account;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
@RequiredArgsConstructor
@Builder
@Getter
@EqualsAndHashCode
public class AccountCreationRequestDto {
    @NotBlank
    private final String profileUuid;

    @NotBlank
    @Email
    private final String email;

    @NotEmpty
    private final String password;
}