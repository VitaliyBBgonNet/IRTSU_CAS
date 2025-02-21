package bbgon.irtsu_cas.controllers;

import bbgon.irtsu_cas.dto.request.AuthUserRequest;
import bbgon.irtsu_cas.dto.request.RegistrationUserRequest;
import bbgon.irtsu_cas.services.impl.ResourceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/authUser")
@RequiredArgsConstructor
public class AuthPageController {
    private final ResourceService resourceService;

    @GetMapping("/login")
    public String loginPage(Model model) {
        model.addAttribute("loginForm", new AuthUserRequest());
        return "loginPage";
    }

    @GetMapping("/profile")
    public String profilePage(Model model) {
        return "profile";
    }

    @PostMapping("/profile")
    public String profileContent(Model model) {

        model.addAttribute("components", resourceService.listAllElements());
        return "profile";
    }

    @GetMapping("/registration")
    public String registration(Model model) {
        model.addAttribute("registerForm", new RegistrationUserRequest());
        return "registration-user";
    }
}
