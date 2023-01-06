package me.tiary.controller;

import lombok.RequiredArgsConstructor;
import me.tiary.dto.account.*;
import me.tiary.properties.jwt.AccessTokenProperties;
import me.tiary.properties.jwt.RefreshTokenProperties;
import me.tiary.service.AccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
    public ResponseEntity<Void> checkEmailExistence(@PathVariable @NotBlank @Email final String email) {
        return (accountService.checkEmailExistence(email)) ? (ResponseEntity.ok().build()) : (ResponseEntity.notFound().build());
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

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody @Valid final AccountLoginRequestDto requestDto, final HttpServletResponse response) {
        final AccountLoginResponseDto responseDto = accountService.login(requestDto);

        final Cookie accessTokenCookie = new Cookie(AccessTokenProperties.COOKIE_NAME, responseDto.getAccessToken());

        accessTokenCookie.setHttpOnly(true);
        accessTokenCookie.setPath("/");
        accessTokenCookie.setSecure(true);

        response.addCookie(accessTokenCookie);

        final Cookie refreshTokenCookie = new Cookie(RefreshTokenProperties.COOKIE_NAME, responseDto.getRefreshToken());

        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setSecure(true);
        refreshTokenCookie.setMaxAge(responseDto.getRefreshTokenValidSeconds());

        response.addCookie(refreshTokenCookie);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/logout")
    public ResponseEntity<Void> logout(final HttpServletRequest request, final HttpServletResponse response) {
        final Cookie accessTokenCookie = new Cookie(AccessTokenProperties.COOKIE_NAME, null);

        accessTokenCookie.setMaxAge(0);
        accessTokenCookie.setHttpOnly(true);
        accessTokenCookie.setPath("/");
        accessTokenCookie.setSecure(true);

        response.addCookie(accessTokenCookie);

        final Cookie refreshTokenCookie = new Cookie(RefreshTokenProperties.COOKIE_NAME, null);

        refreshTokenCookie.setMaxAge(0);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setSecure(true);

        response.addCookie(refreshTokenCookie);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}