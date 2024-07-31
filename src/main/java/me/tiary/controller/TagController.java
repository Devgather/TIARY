package me.tiary.controller;

import lombok.RequiredArgsConstructor;
import me.tiary.dto.tag.TagListReadResponseDto;
import me.tiary.service.TagService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;

@RestController
@RequestMapping("/api/tag")
@Validated
@RequiredArgsConstructor
public class TagController {
    private final TagService tagService;

    @GetMapping("/list/{tilUuid}")
    public ResponseEntity<TagListReadResponseDto> readTagList(@PathVariable @NotBlank final String tilUuid) {
        final TagListReadResponseDto result = tagService.readTagList(tilUuid);

        return ResponseEntity.ok(result);
    }
}