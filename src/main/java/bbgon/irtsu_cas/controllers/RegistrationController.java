package bbgon.irtsu_cas.controllers;

import bbgon.irtsu_cas.dto.request.AuthUserRequest;
import bbgon.irtsu_cas.dto.request.RegistrationUserRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/authUser")
public class RegistrationController {

    @GetMapping("/registration")
    public String registration(Model model) {
        model.addAttribute("registerForm", new RegistrationUserRequest());
        return "registration-user";
    }
}
