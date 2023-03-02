package me.tiary.controller;

import lombok.Getter;
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
    private static final String INDEX_VIEW_REDIRECTION = "redirect:/";

    private final ProfileService profileService;

    private final TilService tilService;

    @GetMapping("/")
    public String directIndexView(@AuthenticationPrincipal final MemberDetails memberDetails,
                                  final Model model) {
        model.addAttribute(ModelCommonAttribute.AUTHENTICATION.getName(), memberDetails != null);

        if (memberDetails != null) {
            final String memberNickname = profileService.searchNicknameUsingUuid(memberDetails.getProfileUuid());

            model.addAttribute(ModelCommonAttribute.NICKNAME.getName(), memberNickname);
        }

        return "view/index";
    }

    @GetMapping("/login")
    public String directLoginView(final Model model) {
        model.addAttribute(ModelCommonAttribute.AUTHENTICATION.getName(), false);

        return "view/login";
    }

    @GetMapping("/register")
    public String directRegisterView(final Model model) {
        model.addAttribute(ModelCommonAttribute.AUTHENTICATION.getName(), false);

        return "view/register";
    }

    @GetMapping("/profile/{nickname}")
    public String directProfileView(@PathVariable @NotBlank @Size(max = Profile.NICKNAME_MAX_LENGTH) final String nickname,
                                    @RequestParam final int page,
                                    @RequestParam final int size,
                                    @AuthenticationPrincipal final MemberDetails memberDetails,
                                    final Model model) {
        if (!profileService.checkNicknameExistence(nickname)) {
            return INDEX_VIEW_REDIRECTION;
        }

        model.addAttribute(ModelCommonAttribute.AUTHENTICATION.getName(), memberDetails != null);

        model.addAttribute(ModelParameterAttribute.PROFILE_NICKNAME.getName(), nickname);
        model.addAttribute(ModelParameterAttribute.EDIT_PERMISSION.getName(), false);

        model.addAttribute(ModelParameterAttribute.PAGE.getName(), page);
        model.addAttribute(ModelParameterAttribute.SIZE.getName(), size);

        if (memberDetails != null) {
            final String memberNickname = profileService.searchNicknameUsingUuid(memberDetails.getProfileUuid());

            model.addAttribute(ModelCommonAttribute.NICKNAME.getName(), memberNickname);

            model.addAttribute(ModelParameterAttribute.EDIT_PERMISSION.getName(), memberNickname.equals(nickname));
        }

        return "view/profile";
    }

    @GetMapping("/profile/editor")
    public String directProfileEditorView(@AuthenticationPrincipal final MemberDetails memberDetails,
                                          final Model model) {
        final String memberNickname = profileService.searchNicknameUsingUuid(memberDetails.getProfileUuid());

        model.addAttribute(ModelCommonAttribute.AUTHENTICATION.getName(), true);
        model.addAttribute(ModelCommonAttribute.NICKNAME.getName(), memberNickname);

        return "view/profile-editor";
    }

    @GetMapping("/til/{uuid}")
    public String directTilView(@PathVariable @NotBlank final String uuid,
                                @RequestParam final int page,
                                @RequestParam final int size,
                                @AuthenticationPrincipal final MemberDetails memberDetails,
                                final Model model) {
        if (!tilService.checkUuidExistence(uuid)) {
            return INDEX_VIEW_REDIRECTION;
        }

        model.addAttribute(ModelCommonAttribute.AUTHENTICATION.getName(), memberDetails != null);

        model.addAttribute(ModelParameterAttribute.TIL_UUID.getName(), uuid);
        model.addAttribute(ModelParameterAttribute.EDIT_PERMISSION.getName(), false);

        model.addAttribute(ModelParameterAttribute.PAGE.getName(), page);
        model.addAttribute(ModelParameterAttribute.SIZE.getName(), size);

        if (memberDetails != null) {
            final String memberNickname = profileService.searchNicknameUsingUuid(memberDetails.getProfileUuid());

            final String author = tilService.searchAuthorUsingUuid(uuid);

            model.addAttribute(ModelCommonAttribute.NICKNAME.getName(), memberNickname);

            model.addAttribute(ModelParameterAttribute.EDIT_PERMISSION.getName(), memberNickname.equals(author));
        }

        return "view/til";
    }

    @GetMapping("/til/editor")
    public String directTilEditorView(@AuthenticationPrincipal final MemberDetails memberDetails,
                                      final Model model) {
        final String memberNickname = profileService.searchNicknameUsingUuid(memberDetails.getProfileUuid());

        model.addAttribute(ModelCommonAttribute.AUTHENTICATION.getName(), true);
        model.addAttribute(ModelCommonAttribute.NICKNAME.getName(), memberNickname);

        return "view/til-editor";
    }

    @GetMapping("/til/editor/{uuid}")
    public String directTilEditorView(@PathVariable @NotBlank final String uuid,
                                      @AuthenticationPrincipal final MemberDetails memberDetails,
                                      final Model model) {
        if (!tilService.checkUuidExistence(uuid)) {
            return INDEX_VIEW_REDIRECTION;
        }

        final String memberNickname = profileService.searchNicknameUsingUuid(memberDetails.getProfileUuid());

        final String author = tilService.searchAuthorUsingUuid(uuid);

        if (!memberNickname.equals(author)) {
            return INDEX_VIEW_REDIRECTION;
        }

        model.addAttribute(ModelCommonAttribute.AUTHENTICATION.getName(), true);
        model.addAttribute(ModelCommonAttribute.NICKNAME.getName(), memberNickname);

        model.addAttribute(ModelParameterAttribute.TIL_UUID.getName(), uuid);

        return "view/til-editor";
    }

    @RequiredArgsConstructor
    @Getter
    private enum ModelCommonAttribute {
        AUTHENTICATION("authentication"),
        NICKNAME("memberNickname");

        private final String name;
    }

    @RequiredArgsConstructor
    @Getter
    private enum ModelParameterAttribute {
        PROFILE_NICKNAME("nickname"),
        TIL_UUID("uuid"),
        EDIT_PERMISSION("editPermission"),
        PAGE("page"),
        SIZE("size");

        private final String name;
    }
}