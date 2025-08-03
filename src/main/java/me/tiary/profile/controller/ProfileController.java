package me.tiary.profile.controller;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import me.tiary.common.dto.ApiResponse;
import me.tiary.profile.controller.dto.NicknameCheckResponse;
import me.tiary.profile.domain.Profile;
import me.tiary.profile.service.ProfileService;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("/api/profiles")
@Validated
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    private final MessageSource messageSource;

    @GetMapping("/nickname/check")
    public ResponseEntity<ApiResponse<NicknameCheckResponse>> checkNickname(@RequestParam @NotBlank(message = "{notblank.profile.nickname}") @Size(max = Profile.MAX_NICKNAME_LENGTH, message = "{size.profile.nickname}") final String nickname,
                                                                            final Locale locale) {
        final boolean available = profileService.isNicknameAvailable(nickname);

        final NicknameCheckResponse data = new NicknameCheckResponse(available);
        final String messageCode = (available) ? ("available.profile.nickname") : ("unavailable.profile.nickname");
        final String message = messageSource.getMessage(messageCode, null, locale);
        final LocalDateTime timestamp = LocalDateTime.now();

        return ResponseEntity.ok(new ApiResponse<>(data, List.of(message), timestamp));
    }

}
