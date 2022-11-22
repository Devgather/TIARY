package me.tiary.controller;

import lombok.RequiredArgsConstructor;
import me.tiary.service.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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
}