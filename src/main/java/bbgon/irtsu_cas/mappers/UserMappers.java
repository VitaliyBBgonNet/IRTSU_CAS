package bbgon.irtsu_cas.mappers;

import bbgon.irtsu_cas.dto.UserRegistrationDTO;
import bbgon.irtsu_cas.entity.Users;
import org.mapstruct.Mapping;

public interface UserMappers {

    //User DTO -> Users
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    Users toUser(UserRegistrationDTO userDTO);

    //User -> User DTO
    UserRegistrationDTO toUserRegistrationDTO(Users users);
}
