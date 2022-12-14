package me.tiary.controller;

import lombok.RequiredArgsConstructor;
import me.tiary.domain.Profile;
import me.tiary.dto.profile.*;
import me.tiary.security.web.userdetails.MemberDetails;
import me.tiary.service.ProfileService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    public ResponseEntity<Void> checkNicknameExistence(@PathVariable @NotBlank @Size(max = Profile.NICKNAME_MAX_LENGTH) final String nickname) {
        return (profileService.checkNicknameExistence(nickname)) ? (ResponseEntity.ok().build()) : (ResponseEntity.notFound().build());
    }

    @PostMapping("")
    public ResponseEntity<ProfileCreationResponseDto> createProfile(@RequestBody @Valid final ProfileCreationRequestDto requestDto) {
        final ProfileCreationResponseDto responseDto = profileService.createProfile(requestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @GetMapping("/{nickname}")
    public ResponseEntity<ProfileReadResponseDto> readProfile(@PathVariable @NotBlank @Size(max = Profile.NICKNAME_MAX_LENGTH) final String nickname) {
        final ProfileReadResponseDto result = profileService.readProfile(nickname);

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @PatchMapping(value = "")
    public ResponseEntity<ProfileUpdateResponseDto> updateProfile(@AuthenticationPrincipal final MemberDetails memberDetails, @RequestBody @Valid final ProfileUpdateRequestDto requestDto) {
        final String profileUuid = memberDetails.getProfileUuid();

        final ProfileUpdateResponseDto result = profileService.updateProfile(profileUuid, requestDto);

        return ResponseEntity.ok().body(result);
    }

    @PatchMapping(value = "/picture", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProfilePictureUploadResponseDto> uploadPicture(@AuthenticationPrincipal final MemberDetails memberDetails, @ModelAttribute final ProfilePictureUploadRequestDto requestDto) {
        final String profileUuid = memberDetails.getProfileUuid();

        final ProfilePictureUploadResponseDto responseDto = profileService.uploadPicture(profileUuid, requestDto);

        return ResponseEntity.ok().body(responseDto);
    }
}