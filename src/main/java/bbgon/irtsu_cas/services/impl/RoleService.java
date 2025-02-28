package bbgon.irtsu_cas.services.impl;

import bbgon.irtsu_cas.CustomException;
import bbgon.irtsu_cas.constants.ErrorCodes;
import bbgon.irtsu_cas.entity.UsersEntity;
import bbgon.irtsu_cas.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final UserService userService;

    public void roleUser(){
        System.out.println();

        UsersEntity thisUser = userService.findUserEntityById(userService.getUserIdByToken());

        System.out.println(thisUser);
        if (!"Admin".equalsIgnoreCase(thisUser.getRole())) {
            throw new CustomException(ErrorCodes.ACCESS_DENIED);
        }
    }
}
