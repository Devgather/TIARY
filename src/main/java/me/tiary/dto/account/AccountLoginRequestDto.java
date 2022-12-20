package me.tiary.dto.account;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Builder
@RequiredArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
@EqualsAndHashCode
public class AccountLoginRequestDto {
    @NotBlank
    @Email
    private final String email;

    @NotBlank
    private final String password;
}