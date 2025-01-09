package bbgon.irtsu_cas.services;

import bbgon.irtsu_cas.dto.CustomDoneRegistrationDTO;
import bbgon.irtsu_cas.dto.UserRegistrationDTO;
import bbgon.irtsu_cas.entity.UsersEntity;

import java.util.UUID;

public interface UserService {
    UsersEntity findUserEntityById(UUID id);

    UUID getUserIdByToken();
}
