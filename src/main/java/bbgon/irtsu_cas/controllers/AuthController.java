package bbgon.irtsu_cas.controllers;

import bbgon.irtsu_cas.dto.request.AuthUserRequest;
import bbgon.irtsu_cas.dto.request.RegistrationUserRequest;
import bbgon.irtsu_cas.dto.response.CustomSuccessResponse;
import bbgon.irtsu_cas.dto.response.LoginUserResponse;
import bbgon.irtsu_cas.services.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<CustomSuccessResponse<LoginUserResponse>> registrationUser(
            @RequestBody
            @Valid RegistrationUserRequest registration) {
        return ResponseEntity.ok(authService.registrationUser(registration));
    }

    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CustomSuccessResponse<LoginUserResponse>> authorizationUser(
            @RequestBody
            @Valid AuthUserRequest authUserRequest) {
        System.out.println("any");
        return ResponseEntity.ok(authService.authorizationUser(authUserRequest));
    }


}
