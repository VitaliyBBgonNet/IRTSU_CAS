package bbgon.irtsu_cas.controllers.restControllers;

import bbgon.irtsu_cas.entity.UsersEntity;
import bbgon.irtsu_cas.services.UserService;
import bbgon.irtsu_cas.services.impl.ProfileDataServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/data")
@RequiredArgsConstructor
public class ProfileController {
    // Тут мы возвращаем информацию о пользователе
    private final ProfileDataServiceImpl profileDataService;

    @GetMapping("/profile")
    public String getProfileData(@RequestHeader("Authorization") String token) {
        return profileDataService.getProfileFIO();
    }

}
