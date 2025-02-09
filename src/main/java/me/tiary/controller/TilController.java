package me.tiary.controller;

import lombok.RequiredArgsConstructor;
import me.tiary.domain.Profile;
import me.tiary.dto.til.*;
import me.tiary.security.web.userdetails.MemberDetails;
import me.tiary.service.TilService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDate;

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
                                                              @RequestParam(required = false) final String tag,
                                                              @RequestParam final int page,
                                                              @RequestParam final int size) {
        final Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());

        final TilListReadResponseDto result = (tag == null) ? (tilService.readTilList(nickname, pageable)) : (tilService.readTilListByTag(nickname, tag, pageable));

        return ResponseEntity.ok(result);
    }

    @GetMapping("/list")
    public ResponseEntity<RecentTilListReadResponseDto> readRecentTilList(@RequestParam final int size) {
        final Pageable pageable = PageRequest.of(0, size, Sort.by("createdDate").descending());

        final RecentTilListReadResponseDto result = tilService.readRecentTilList(pageable);

        return ResponseEntity.ok(result);
    }

    @PutMapping("/{uuid}")
    public ResponseEntity<TilEditResponseDto> updateTil(@PathVariable @NotBlank final String uuid,
                                                        @AuthenticationPrincipal final MemberDetails memberDetails,
                                                        @RequestBody @Valid final TilEditRequestDto requestDto) {
        final String profileUuid = memberDetails.getProfileUuid();

        final TilEditResponseDto result = tilService.updateTil(profileUuid, uuid, requestDto);

        return ResponseEntity.ok().body(result);
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<TilDeletionResponseDto> deleteTil(@AuthenticationPrincipal final MemberDetails memberDetails,
                                                            @PathVariable @NotBlank final String uuid) {
        final String profileUuid = memberDetails.getProfileUuid();

        final TilDeletionResponseDto result = tilService.deleteTil(profileUuid, uuid);

        return ResponseEntity.ok(result);
    }

    @GetMapping("/streak/{nickname}")
    public ResponseEntity<TilStreakReadResponseDto> readTilStreak(@PathVariable @NotBlank @Size(max = Profile.NICKNAME_MAX_LENGTH) final String nickname,
                                                                  @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") final LocalDate startDate,
                                                                  @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") final LocalDate endDate) {
        final TilStreakReadResponseDto result = tilService.readTilStreak(nickname, startDate, endDate);

        return ResponseEntity.ok(result);
    }
}