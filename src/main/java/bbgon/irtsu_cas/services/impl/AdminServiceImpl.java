package bbgon.irtsu_cas.services.impl;

import bbgon.irtsu_cas.Constants;
import bbgon.irtsu_cas.CustomException;
import bbgon.irtsu_cas.constants.ErrorCodes;
import bbgon.irtsu_cas.constants.StringConstants;
import bbgon.irtsu_cas.constants.ValidationConstants;
import bbgon.irtsu_cas.dto.request.AddNewUserFromAdmin;
import bbgon.irtsu_cas.dto.request.UpdateUser;
import bbgon.irtsu_cas.dto.response.CustomSuccessResponse;
import bbgon.irtsu_cas.dto.response.SuccessResponse;
import bbgon.irtsu_cas.dto.response.Users;
import bbgon.irtsu_cas.entity.UsersEntity;
import bbgon.irtsu_cas.repositories.AuthRepository;
import bbgon.irtsu_cas.repositories.UserRepository;
import bbgon.irtsu_cas.services.AdminService;
import bbgon.irtsu_cas.services.UserService;
import bbgon.irtsu_cas.utils.AESUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final UserService userService;

    private final AuthRepository authRepository;

    private final UserRepository userRepository;

    private final AESUtil aesUtil;

    @Transactional
    @Override
    public CustomSuccessResponse<SuccessResponse> updateUserData(UpdateUser updateUser) {
        UsersEntity userAdmin = userService.findUserEntityById(userService.getUserIdByToken());
        UsersEntity usersEntityUpdate = userService.findUserEntityById(UUID.fromString(updateUser.getId()));

        if (!StringConstants.ADMIN_ROLE.equalsIgnoreCase(userAdmin.getRole())) {
            throw new CustomException(ErrorCodes.ACCESS_DENIED);
        }

        usersEntityUpdate.setName(updateUser.getFirstName());
        usersEntityUpdate.setLastName(updateUser.getLastName());
        usersEntityUpdate.setSurname(updateUser.getSurName());
        usersEntityUpdate.setEmail(updateUser.getEmail());
        usersEntityUpdate.setPhone(updateUser.getPhoneNumber());

        if (updateUser.getPassword() != null && !updateUser.getPassword().isEmpty()) {
            usersEntityUpdate.setPassword(aesUtil.encrypt(updateUser.getPassword()));
        }

        usersEntityUpdate.setAddInformation(updateUser.getAddInformation());

        userRepository.save(usersEntityUpdate);

        return new CustomSuccessResponse<>(new SuccessResponse());
    }


    @Override
    public CustomSuccessResponse<SuccessResponse> createNewUser(AddNewUserFromAdmin addNewUserFromAdmin) {

        UsersEntity thisUser = userService.findUserEntityById(userService.getUserIdByToken());

        if (!StringConstants.ADMIN_ROLE.equalsIgnoreCase(thisUser.getRole())) {
            throw new CustomException(ErrorCodes.ACCESS_DENIED);
        }

        if (authRepository.findByEmail(addNewUserFromAdmin.getEmail()).isPresent()) {
            throw new CustomException(ErrorCodes.USER_ALREADY_EXISTS);
        }

        String password = aesUtil.encrypt(addNewUserFromAdmin.getPassword());

        UsersEntity usersEntity = new UsersEntity();
        usersEntity.setEmail(addNewUserFromAdmin.getEmail());
        usersEntity.setPassword(password);
        usersEntity.setName(addNewUserFromAdmin.getName());
        usersEntity.setLastName(addNewUserFromAdmin.getLastName());
        usersEntity.setSurname(addNewUserFromAdmin.getSurName());
        usersEntity.setPhone(addNewUserFromAdmin.getPhoneNumber());
        usersEntity.setRole(StringConstants.TEACHER_ROLE);
        usersEntity.setAddInformation(addNewUserFromAdmin.getAddInformation());

        authRepository.save(usersEntity);

        return new CustomSuccessResponse<>(new SuccessResponse());
    }

    @Override
    public List<Users> getAllUsersForAdmin() {

        UsersEntity thisUser = userService.findUserEntityById(userService.getUserIdByToken());

        if (!StringConstants.ADMIN_ROLE.equalsIgnoreCase(thisUser.getRole())) {
            throw new CustomException(ErrorCodes.ACCESS_DENIED);
        }

        List<UsersEntity> usersEntities = userRepository.findByRoleNot(StringConstants.ADMIN_ROLE);

        Stream<Users> usersStream = usersEntities.stream()
                .map(usersEntity -> {
                    return new Users(
                            usersEntity.getId().toString(),
                            Optional.ofNullable(usersEntity.getLastName()).orElse("") +
                                    " " +
                                    Optional.ofNullable(usersEntity.getName()).orElse("")
                                    + " " +
                                    Optional.ofNullable(usersEntity.getSurname()).orElse(""),

                            Optional.ofNullable(usersEntity.getEmail()).orElse("-"),
                            aesUtil.decrypt(usersEntity.getPassword()),
                            Optional.ofNullable(usersEntity.getPhone()).orElse("-"),
                            Optional.ofNullable(usersEntity.getAddInformation()).orElse("-")
                    );
                });
        return usersStream.collect(Collectors.toList());
    }

    @Override
    public CustomSuccessResponse<SuccessResponse> deleteUserById(String id) {

        UUID userId = UUID.fromString(id);
        UsersEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCodes.USER_NOT_FOUND));

        userRepository.delete(user);

        return new CustomSuccessResponse<>(new SuccessResponse("User deleted successfully"));
    }
}
