package me.tiary.controller;

import lombok.RequiredArgsConstructor;
import me.tiary.domain.Profile;
import me.tiary.security.web.userdetails.MemberDetails;
import me.tiary.service.ProfileService;
import me.tiary.service.TilService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Controller
@Validated
@RequiredArgsConstructor
public class ViewController {
    private final ProfileService profileService;

    private final TilService tilService;

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
                                    @RequestParam final int page,
                                    @RequestParam final int size,
                                    @AuthenticationPrincipal final MemberDetails memberDetails,
                                    final Model model) {
        if (!profileService.checkNicknameExistence(nickname)) {
            return "redirect:/";
        }

        model.addAttribute("authentication", memberDetails != null);

        model.addAttribute("nickname", nickname);
        model.addAttribute("editPermission", false);

        model.addAttribute("page", page);
        model.addAttribute("size", size);

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

    @GetMapping("/til/{uuid}")
    public String directTilView(@PathVariable @NotBlank final String uuid,
                                @RequestParam final int page,
                                @RequestParam final int size,
                                @AuthenticationPrincipal final MemberDetails memberDetails,
                                final Model model) {
        if (!tilService.checkUuidExistence(uuid)) {
            return "redirect:/";
        }

        model.addAttribute("authentication", memberDetails != null);

        model.addAttribute("uuid", uuid);
        model.addAttribute("editPermission", false);

        model.addAttribute("page", page);
        model.addAttribute("size", size);

        if (memberDetails != null) {
            final String memberNickname = profileService.searchNicknameUsingUuid(memberDetails.getProfileUuid());

            final String author = tilService.searchAuthorUsingUuid(uuid);

            model.addAttribute("memberNickname", memberNickname);

            model.addAttribute("editPermission", memberNickname.equals(author));
        }

        return "view/til";
    }

    @GetMapping("/til/editor")
    public String directTilEditorView(@AuthenticationPrincipal final MemberDetails memberDetails,
                                      final Model model) {
        final String memberNickname = profileService.searchNicknameUsingUuid(memberDetails.getProfileUuid());

        model.addAttribute("authentication", true);
        model.addAttribute("memberNickname", memberNickname);

        return "view/til-editor";
    }

    @GetMapping("/til/editor/{uuid}")
    public String directTilEditorView(@PathVariable @NotBlank final String uuid,
                                      @AuthenticationPrincipal final MemberDetails memberDetails,
                                      final Model model) {
        if (!tilService.checkUuidExistence(uuid)) {
            return "redirect:/";
        }

        final String memberNickname = profileService.searchNicknameUsingUuid(memberDetails.getProfileUuid());

        final String author = tilService.searchAuthorUsingUuid(uuid);

        if (!memberNickname.equals(author)) {
            return "redirect:/";
        }

        model.addAttribute("authentication", true);
        model.addAttribute("memberNickname", memberNickname);

        model.addAttribute("uuid", uuid);

        return "view/til-editor";
    }
}