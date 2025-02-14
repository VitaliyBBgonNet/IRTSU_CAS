package bbgon.irtsu_cas.controllers;

import bbgon.irtsu_cas.dto.request.AuthUserRequest;
import bbgon.irtsu_cas.entity.UsersEntity;
import bbgon.irtsu_cas.services.AuthService;
import bbgon.irtsu_cas.services.UserService;
import bbgon.irtsu_cas.services.impl.ResourceService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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


    private final AuthService authService;

    private final UserService userService;

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
}
