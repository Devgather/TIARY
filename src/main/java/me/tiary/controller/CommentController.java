package me.tiary.controller;

import lombok.RequiredArgsConstructor;
import me.tiary.dto.comment.CommentDeletionResponseDto;
import me.tiary.dto.comment.CommentListReadResponseDto;
import me.tiary.dto.comment.CommentWritingRequestDto;
import me.tiary.dto.comment.CommentWritingResponseDto;
import me.tiary.security.web.userdetails.MemberDetails;
import me.tiary.service.CommentService;
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

@RestController
@RequestMapping("/api/comment")
@Validated
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @PostMapping("")
    public ResponseEntity<CommentWritingResponseDto> writeComment(@AuthenticationPrincipal final MemberDetails memberDetails, @RequestBody @Valid final CommentWritingRequestDto requestDto) {
        final String profileUuid = memberDetails.getProfileUuid();

        final CommentWritingResponseDto responseDto = commentService.writeComment(profileUuid, requestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @GetMapping("/list/{tilUuid}")
    public ResponseEntity<CommentListReadResponseDto> readCommentList(@PathVariable @NotBlank final String tilUuid,
                                                                      @RequestParam final int page,
                                                                      @RequestParam final int size) {
        final Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());

        final CommentListReadResponseDto result = commentService.readCommentList(tilUuid, pageable);

        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<CommentDeletionResponseDto> deleteComment(@AuthenticationPrincipal final MemberDetails memberDetails,
                                                                    @PathVariable @NotBlank final String uuid) {
        final String profileUuid = memberDetails.getProfileUuid();

        final CommentDeletionResponseDto result = commentService.deleteComment(profileUuid, uuid);

        return ResponseEntity.ok(result);
    }
}