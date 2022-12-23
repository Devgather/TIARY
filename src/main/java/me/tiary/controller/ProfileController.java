package me.tiary.controller;

import lombok.RequiredArgsConstructor;
import me.tiary.domain.Profile;
import me.tiary.dto.profile.ProfileCreationRequestDto;
import me.tiary.dto.profile.ProfileCreationResponseDto;
import me.tiary.dto.profile.ProfileReadResponseDto;
import me.tiary.service.ProfileService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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

    @PostMapping("")
    public ResponseEntity<ProfileCreationResponseDto> createProfile(@RequestBody @Valid final ProfileCreationRequestDto requestDto) {
        final ProfileCreationResponseDto responseDto = profileService.createProfile(requestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @GetMapping("/nickname/{nickname}")
    public ResponseEntity<ProfileReadResponseDto> readProfile(@PathVariable final String nickname) {
        final ProfileReadResponseDto responseDto = profileService.readProfile(nickname);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }
}