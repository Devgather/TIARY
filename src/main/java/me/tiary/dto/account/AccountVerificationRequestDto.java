package me.tiary.dto.account;

import lombok.*;
import me.tiary.domain.Verification;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
@RequiredArgsConstructor
@Builder
@Getter
@EqualsAndHashCode
public class AccountVerificationRequestDto {
    @NotBlank
    @Email
    private final String email;

    @NotBlank
    @Size(min = Verification.CODE_MAX_LENGTH, max = Verification.CODE_MAX_LENGTH)
    private final String code;
}