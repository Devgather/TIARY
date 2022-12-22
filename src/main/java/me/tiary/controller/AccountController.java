package me.tiary.controller;

import lombok.RequiredArgsConstructor;
import me.tiary.dto.account.AccountCreationRequestDto;
import me.tiary.dto.account.AccountCreationResponseDto;
import me.tiary.dto.account.AccountVerificationRequestDto;
import me.tiary.dto.account.AccountVerificationResponseDto;
import me.tiary.service.AccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@RestController
@RequestMapping("/api/account")
@Validated
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;

    @RequestMapping(value = "/email/{email}", method = RequestMethod.HEAD)
    public ResponseEntity<Void> checkEmailDuplication(@PathVariable @NotBlank @Email final String email) {
        return (accountService.checkEmailDuplication(email)) ? (ResponseEntity.ok().build()) : (ResponseEntity.notFound().build());
    }

    @PostMapping("")
    public ResponseEntity<AccountCreationResponseDto> register(@RequestBody @Valid final AccountCreationRequestDto requestDto) {
        final AccountCreationResponseDto result = accountService.register(requestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @PostMapping("/verification/{email}")
    public ResponseEntity<Void> sendVerificationMail(@PathVariable @NotBlank @Email final String email) throws MessagingException {
        accountService.sendVerificationMail(email);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PatchMapping("/verification")
    public ResponseEntity<AccountVerificationResponseDto> verifyEmail(@RequestBody @Valid final AccountVerificationRequestDto requestDto) {
        final AccountVerificationResponseDto result = accountService.verifyEmail(requestDto);

        return ResponseEntity.ok(result);
    }
}