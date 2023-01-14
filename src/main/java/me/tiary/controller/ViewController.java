package me.tiary.controller;

import lombok.RequiredArgsConstructor;
import me.tiary.domain.Profile;
import me.tiary.security.web.userdetails.MemberDetails;
import me.tiary.service.ProfileService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Controller
@Validated
@RequiredArgsConstructor
public class ViewController {
    private final ProfileService profileService;

    @GetMapping("/")
    public String directIndexView(@AuthenticationPrincipal final MemberDetails memberDetails,
                                  final Model model) {
        model.addAttribute("authentication", memberDetails != null);

        if (memberDetails != null) {
            final String memberNickname = profileService.searchNicknameUsingUuid(memberDetails.getProfileUuid());

            model.addAttribute("memberNickname", memberNickname);
        }

        return "view/index";
    }

    @GetMapping("/login")
    public String directLoginView(final Model model) {
        model.addAttribute("authentication", false);

        return "view/login";
    }

    @GetMapping("/register")
    public String directRegisterView(final Model model) {
        model.addAttribute("authentication", false);

        return "view/register";
    }

    @GetMapping("/profile/{nickname}")
    public String directProfileView(@PathVariable @NotBlank @Size(max = Profile.NICKNAME_MAX_LENGTH) final String nickname,
                                    @AuthenticationPrincipal final MemberDetails memberDetails,
                                    final Model model) {
        if (!profileService.checkNicknameExistence(nickname)) {
            return "redirect:/";
        }

        model.addAttribute("authentication", memberDetails != null);

        model.addAttribute("nickname", nickname);
        model.addAttribute("editPermission", false);

        if (memberDetails != null) {
            final String memberNickname = profileService.searchNicknameUsingUuid(memberDetails.getProfileUuid());

            model.addAttribute("memberNickname", memberNickname);

            model.addAttribute("editPermission", memberNickname.equals(nickname));
        }

        return "view/profile";
    }

    @GetMapping("/profile/editor")
    public String directProfileEditorView(@AuthenticationPrincipal final MemberDetails memberDetails,
                                          final Model model) {
        final String memberNickname = profileService.searchNicknameUsingUuid(memberDetails.getProfileUuid());

        model.addAttribute("authentication", true);
        model.addAttribute("memberNickname", memberNickname);

        return "view/profile-editor";
    }

    @GetMapping("/til/editor")
    public String directTilEditorView(@AuthenticationPrincipal final MemberDetails memberDetails,
                                      final Model model) {
        final String memberNickname = profileService.searchNicknameUsingUuid(memberDetails.getProfileUuid());

        model.addAttribute("authentication", true);
        model.addAttribute("memberNickname", memberNickname);

        return "view/til-editor";
    }
}