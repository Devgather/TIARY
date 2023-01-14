package me.tiary.controller;

import lombok.RequiredArgsConstructor;
import me.tiary.dto.til.TilReadResponseDto;
import me.tiary.dto.til.TilWritingRequestDto;
import me.tiary.dto.til.TilWritingResponseDto;
import me.tiary.security.web.userdetails.MemberDetails;
import me.tiary.service.TilService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@RestController
@RequestMapping("/api/til")
@Validated
@RequiredArgsConstructor
public class TilController {
    private final TilService tilService;

    @PostMapping("")
    public ResponseEntity<TilWritingResponseDto> writeTil(@AuthenticationPrincipal final MemberDetails memberDetails, @RequestBody @Valid final TilWritingRequestDto requestDto) {
        final String profileUuid = memberDetails.getProfileUuid();

        final TilWritingResponseDto result = tilService.writeTil(profileUuid, requestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<TilReadResponseDto> readTil(@PathVariable @NotBlank final String uuid) {
        final TilReadResponseDto result = tilService.readTil(uuid);

        return ResponseEntity.ok(result);
    }
}