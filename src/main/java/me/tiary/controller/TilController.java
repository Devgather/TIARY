package me.tiary.controller;

import lombok.RequiredArgsConstructor;
import me.tiary.dto.til.TilWritingRequestDto;
import me.tiary.dto.til.TilWritingResponseDto;
import me.tiary.security.web.userdetails.MemberDetails;
import me.tiary.service.TilService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/til")
@RequiredArgsConstructor
public class TilController {
    private final TilService tilService;

    @PostMapping("")
    public ResponseEntity<TilWritingResponseDto> writeTil(@AuthenticationPrincipal final MemberDetails memberDetails, @RequestBody @Valid final TilWritingRequestDto requestDto) {
        final String profileUuid = memberDetails.getProfileUuid();

        final TilWritingResponseDto result = tilService.writeTil(profileUuid, requestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }
}