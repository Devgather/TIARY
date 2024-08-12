package me.tiary.controller;

import lombok.RequiredArgsConstructor;
import me.tiary.dto.tag.TagListReadResponseDto;
import me.tiary.dto.tag.TagListWritingRequestDto;
import me.tiary.security.web.userdetails.MemberDetails;
import me.tiary.service.TagService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@RestController
@RequestMapping("/api/tag")
@Validated
@RequiredArgsConstructor
public class TagController {
    private final TagService tagService;

    @PostMapping("/list/{tilUuid}")
    public ResponseEntity<Void> writeTagList(@AuthenticationPrincipal final MemberDetails memberDetails,
                                             @PathVariable @NotBlank final String tilUuid,
                                             @RequestBody @Valid final TagListWritingRequestDto requestDto) {
        final String profileUuid = memberDetails.getProfileUuid();

        tagService.writeTagList(profileUuid, tilUuid, requestDto);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/list/{tilUuid}")
    public ResponseEntity<TagListReadResponseDto> readTagList(@PathVariable @NotBlank final String tilUuid) {
        final TagListReadResponseDto result = tagService.readTagList(tilUuid);

        return ResponseEntity.ok(result);
    }
}