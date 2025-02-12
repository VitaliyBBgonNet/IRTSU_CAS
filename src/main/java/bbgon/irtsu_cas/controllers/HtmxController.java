package bbgon.irtsu_cas.controllers;

import bbgon.irtsu_cas.dto.request.AuthUserRequest;
import bbgon.irtsu_cas.dto.response.CustomSuccessResponse;
import bbgon.irtsu_cas.dto.response.LoginUserResponse;
import bbgon.irtsu_cas.services.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class HtmxController {
    private final AuthService authService;

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("loginForm", new AuthUserRequest());
        return "login";
    }
}
