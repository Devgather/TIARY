package me.tiary.controller;

import lombok.RequiredArgsConstructor;
import me.tiary.domain.Profile;
import me.tiary.dto.til.TilListReadResponseDto;
import me.tiary.dto.til.TilReadResponseDto;
import me.tiary.dto.til.TilWritingRequestDto;
import me.tiary.dto.til.TilWritingResponseDto;
import me.tiary.security.web.userdetails.MemberDetails;
import me.tiary.service.TilService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

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

    @GetMapping("/list/{nickname}")
    public ResponseEntity<TilListReadResponseDto> readTilList(@PathVariable @NotBlank @Size(max = Profile.NICKNAME_MAX_LENGTH) final String nickname,
                                                              @RequestParam final int page,
                                                              @RequestParam final int size) {
        final Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());

        final TilListReadResponseDto result = tilService.readTilList(nickname, pageable);

        return ResponseEntity.ok(result);
    }
}