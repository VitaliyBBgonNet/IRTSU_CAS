package bbgon.irtsu_cas.services.impl;

import bbgon.irtsu_cas.dto.FIO;
import bbgon.irtsu_cas.entity.UsersEntity;
import bbgon.irtsu_cas.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProfileDataServiceImpl {

    private final UserService userService;

    public String getProfileFIO(){
        UsersEntity usersEntity = userService.findUserEntityById(userService.getUserIdByToken());
        return usersEntity.getLastName()+" "+usersEntity.getName()+" "+usersEntity.getSurname();
    }
}
