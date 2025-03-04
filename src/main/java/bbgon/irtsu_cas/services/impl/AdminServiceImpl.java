package bbgon.irtsu_cas.services.impl;

import bbgon.irtsu_cas.CustomException;
import bbgon.irtsu_cas.constants.ErrorCodes;
import bbgon.irtsu_cas.dto.request.AddNewUserFromAdmin;
import bbgon.irtsu_cas.dto.request.GroupEditDataRequest;
import bbgon.irtsu_cas.dto.request.NewGroupRequest;
import bbgon.irtsu_cas.dto.request.UpdateUser;
import bbgon.irtsu_cas.dto.response.CustomSuccessResponse;
import bbgon.irtsu_cas.dto.response.LoginUserResponse;
import bbgon.irtsu_cas.dto.response.SuccessResponse;
import bbgon.irtsu_cas.dto.response.Users;
import bbgon.irtsu_cas.entity.DetailsEntity;
import bbgon.irtsu_cas.entity.GroupEntity;
import bbgon.irtsu_cas.entity.UsersEntity;
import bbgon.irtsu_cas.repositories.AuthRepository;
import bbgon.irtsu_cas.repositories.DetailsRepository;
import bbgon.irtsu_cas.repositories.GroupRepository;
import bbgon.irtsu_cas.repositories.UserRepository;
import bbgon.irtsu_cas.services.AdminService;
import bbgon.irtsu_cas.services.UserService;
import bbgon.irtsu_cas.utils.AESUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final GroupRepository groupRepository;

    private final DetailsRepository detailsRepository;

    private final UserService userService;

    private final AuthRepository authRepository;

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    private final AESUtil aesUtil;

    @Transactional
    @Override
    public CustomSuccessResponse<SuccessResponse> updateUserData(UpdateUser updateUser) {
        UsersEntity userAdmin = userService.findUserEntityById(userService.getUserIdByToken());
        UsersEntity usersEntityUpdate = userService.findUserEntityById(UUID.fromString(updateUser.getId()));

        if (!"Admin".equalsIgnoreCase(userAdmin.getRole())) {
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

        if (!"Admin".equalsIgnoreCase(thisUser.getRole())) {
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
        usersEntity.setRole("Teacher");// перенести в константы
        usersEntity.setAddInformation(addNewUserFromAdmin.getAddInformation());

        authRepository.save(usersEntity);

        return new CustomSuccessResponse<>(new SuccessResponse());
    }

    @Override
    public List<Users> getAllUsersForAdmin() {

        UsersEntity thisUser = userService.findUserEntityById(userService.getUserIdByToken());

        if (!"Admin".equalsIgnoreCase(thisUser.getRole())) {
            throw new CustomException(ErrorCodes.ACCESS_DENIED);
        }

        List<UsersEntity> usersEntities = userRepository.findAll();

        Stream<Users> usersStream = usersEntities.stream().map(usersEntity -> {
            return new Users(
                    usersEntity.getId().toString(),
                    usersEntity.getLastName() + " " + usersEntity.getName() + " " + usersEntity.getSurname(),
                    usersEntity.getEmail(),
                    aesUtil.decrypt(usersEntity.getPassword()),
                    usersEntity.getPhone(),
                    usersEntity.getAddInformation()
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


    @Override
    public CustomSuccessResponse<SuccessResponse> createNewGroup(NewGroupRequest newGroupRequest) {

        UsersEntity thisUser = userService.findUserEntityById(userService.getUserIdByToken());

        groupRepository.findByName(newGroupRequest.getGroupName()).ifPresent(group -> {
            throw new CustomException(ErrorCodes.USER_ALREADY_EXISTS); // исправить на другую кастомную ошибку
        });

        GroupEntity groupEntity = new GroupEntity();
        groupEntity.setName(newGroupRequest.getGroupName());
        groupEntity.setDescription(newGroupRequest.getDescription());
        groupEntity.setAdmin(thisUser);
        Set<UsersEntity> firstUser = new HashSet<>();
        firstUser.add(thisUser);
        groupEntity.setUsers(firstUser);

        groupRepository.save(groupEntity);

        return new CustomSuccessResponse<>(new SuccessResponse("Created new group successfully"));
    }

    @Override
    @Transactional
    public CustomSuccessResponse<SuccessResponse> deleteGroup(UUID groupId, String groupName) {

        GroupEntity groupEntity = findGroupByUUID(groupId);

        if(!groupEntity.getAdmin().getId().equals(userService.getUserIdByToken())){
            throw new CustomException(ErrorCodes.ACCESS_DENIED);//Перепроверить ошибку
        }

        groupRepository.findByNameAndId(groupName, groupId).ifPresent(group -> {
            groupRepository.deleteById(groupId);
        });

        return new CustomSuccessResponse<>(new SuccessResponse("Deleted group successfully"));
    }

    @Override
    @Transactional
    public CustomSuccessResponse<SuccessResponse> editGroupProperties(UUID groupId, GroupEditDataRequest groupEditDataRequest) {
        //Проверим есть ли группа которую мы хотим изменить и достаём её
        //Проверим право на группу
        //Достанем новое имя и проверим используется ли оно уже. Если есть бросим ошибку
        //Установим новые данные если они есть

        GroupEntity groupEntity = findGroupByUUID(groupId);

        if(!groupEntity.getAdmin().getId().equals(userService.getUserIdByToken())){
            throw new CustomException(ErrorCodes.ACCESS_DENIED);//Перепроверить ошибку
        }

        groupRepository.findByNameAndId(groupEditDataRequest.getGroupName(), groupId).ifPresent(group -> {
            throw new CustomException(ErrorCodes.GROUP_NAME_ALREADY_TAKEN);//Имя группы занято
        });

        if(!groupEditDataRequest.getGroupName().isEmpty()){
            groupEntity.setName(groupEditDataRequest.getGroupName());
        }

        if(!groupEditDataRequest.getDescription().isEmpty()){
            groupEntity.setDescription(groupEditDataRequest.getDescription());
        }

        if (!groupEditDataRequest.getLogo().isEmpty()){
            groupEntity.setLogo(groupEditDataRequest.getLogo());
        }

        groupRepository.save(groupEntity);

        return new CustomSuccessResponse<>(new SuccessResponse("Edit group successfully"));
    }

    @Override
    @Transactional
    public CustomSuccessResponse<SuccessResponse> addDetailsInOwnGroup(UUID groupId, UUID detailId) {
        UUID currentUserId = userService.getUserIdByToken();

        GroupEntity groupEntity = findGroupByUUID(groupId);
        if (!groupEntity.getAdmin().getId().equals(currentUserId)) {
            throw new CustomException(ErrorCodes.ACCESS_DENIED);// Поменять логику. Деталь в группу может добавить
                                                                // любой пользователь состоящий в данной группе.
        }

        DetailsEntity detailsEntity = detailsRepository.findById(detailId)
                .orElseThrow(() -> new CustomException(ErrorCodes.DETAIL_NOT_FOUND));

        if (!detailsEntity.getOwner().getId().equals(currentUserId)) {
            throw new CustomException(ErrorCodes.ACCESS_DENIED);
        }

        // Добавить деталь в группу и сохранить изменения
        detailsEntity.setGroup(groupEntity);
        detailsRepository.save(detailsEntity);

        return new CustomSuccessResponse<>(new SuccessResponse("Details added successfully"));
    }

    @Override
    @Transactional
    public CustomSuccessResponse<SuccessResponse> addUserInGroup(UUID uuidUser , UUID uuidGroup){

        GroupEntity groupEntity = findGroupByUUID(uuidGroup);
        UsersEntity thisUser = userService.findUserEntityById(uuidUser);

        if(!groupEntity.getAdmin().getId().equals(userService.getUserIdByToken())){
            throw new CustomException(ErrorCodes.ACCESS_DENIED);//Перепроверить ошибку
        }

        groupEntity.getUsers().add(thisUser);

        groupRepository.save(groupEntity);

        return new CustomSuccessResponse<>(new SuccessResponse("User added successfully"));
    }

    public CustomSuccessResponse<SuccessResponse> deleteDetailFromOwnGroup(UUID uuidDetail, UUID uuidGroup) {

        GroupEntity groupEntity = findGroupByUUID(uuidGroup);

        DetailsEntity detailEntity = detailsRepository.findById(uuidDetail)
                .orElseThrow(() -> new CustomException(ErrorCodes.DETAIL_NOT_FOUND));

        UUID currentUserId = userService.getUserIdByToken();

        // Проверка: является ли пользователь владельцем детали или администратором группы
        boolean isOwner = detailEntity.getOwner().getId().equals(currentUserId);
        boolean isAdmin = groupEntity.getAdmin().getId().equals(currentUserId);

        if (!isOwner && !isAdmin) {
            throw new CustomException(ErrorCodes.ACCESS_DENIED);
        }

        // Проверка: является ли пользователь членом группы (администратор уже проверен)
        boolean isUserInGroup = groupEntity.getUsers().stream()
                .anyMatch(user -> user.getId().equals(currentUserId));

        if (!isUserInGroup) {
            throw new CustomException(ErrorCodes.ACCESS_DENIED);
        }

        // Удаление связи между деталью и группой
        detailEntity.setGroup(null);
        detailsRepository.save(detailEntity);

        return new CustomSuccessResponse<>(new SuccessResponse("Details deleted successfully"));
    }

    @Override
    @Transactional
    public CustomSuccessResponse<SuccessResponse> deleteUserFromOwnGroup(UUID uuidUser , UUID uuidGroup){

        GroupEntity groupEntity = findGroupByUUID(uuidGroup);

        UsersEntity thisUser = userService.findUserEntityById(uuidUser);

        if(!groupEntity.getAdmin().getId().equals(userService.getUserIdByToken())){
            throw new CustomException(ErrorCodes.ACCESS_DENIED);//Перепроверить ошибку
        }

        groupEntity.getUsers().remove(thisUser);

        groupRepository.save(groupEntity);

        return new CustomSuccessResponse<>(new SuccessResponse("User deleted successfully"));
    }

    private GroupEntity findGroupByUUID(UUID uuid) {
        return groupRepository.findById(uuid)
                .orElseThrow(() -> new CustomException(ErrorCodes.NEWS_NOT_FOUND));
    }
}
