package bbgon.irtsu_cas.mappers;

import bbgon.irtsu_cas.dto.UserRegistrationDTO;
import bbgon.irtsu_cas.entity.UsersEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMappers {

    //User DTO -> Users
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    UsersEntity toUser(UserRegistrationDTO userDTO);

    //User -> User DTO
    UserRegistrationDTO toUserRegistrationDTO(UsersEntity usersEntity);
}
