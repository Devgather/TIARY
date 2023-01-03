package me.tiary.controller;

import me.tiary.security.web.userdetails.MemberDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {
    @GetMapping("/")
    public String directIndexView(@AuthenticationPrincipal final MemberDetails memberDetails,
                                  final Model model) {
        model.addAttribute("authentication", memberDetails != null);

        return "view/index";
    }

    @GetMapping("/login")
    public String directLoginView() {
        return "view/login";
    }
}