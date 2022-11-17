package me.tiary.controller;

import lombok.RequiredArgsConstructor;
import me.tiary.domain.Profile;
import me.tiary.service.ProfileService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@RestController
@RequestMapping("/api/profile")
@Validated
@RequiredArgsConstructor
public class ProfileController {
    private final ProfileService profileService;

    @RequestMapping(value = "/nickname/{nickname}", method = RequestMethod.HEAD)
    public ResponseEntity<Void> checkNicknameDuplication(@PathVariable @NotBlank @Size(max = Profile.NICKNAME_MAX_LENGTH) final String nickname) {
        return (profileService.checkNicknameDuplication(nickname)) ? (ResponseEntity.ok().build()) : (ResponseEntity.notFound().build());
    }
}
