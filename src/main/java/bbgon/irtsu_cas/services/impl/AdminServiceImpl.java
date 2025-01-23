package bbgon.irtsu_cas.services.impl;

import bbgon.irtsu_cas.CustomException;
import bbgon.irtsu_cas.constants.ErrorCodes;
import bbgon.irtsu_cas.dto.request.GroupEditDataRequest;
import bbgon.irtsu_cas.dto.request.NewGroupRequest;
import bbgon.irtsu_cas.dto.response.CustomSuccessResponse;
import bbgon.irtsu_cas.dto.response.SuccessResponse;
import bbgon.irtsu_cas.entity.DetailsEntity;
import bbgon.irtsu_cas.entity.GroupEntity;
import bbgon.irtsu_cas.entity.UsersEntity;
import bbgon.irtsu_cas.repositories.DetailsRepository;
import bbgon.irtsu_cas.repositories.GroupRepository;
import bbgon.irtsu_cas.repositories.UserRepository;
import bbgon.irtsu_cas.services.AdminService;
import bbgon.irtsu_cas.services.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final GroupRepository groupRepository;

    private final DetailsRepository detailsRepository;

    private final UserService userService;

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
    public CustomSuccessResponse<SuccessResponse> addDetailsInOwnGroup(UUID groupId, UUID detailId) {
        UUID currentUserId = userService.getUserIdByToken();


        GroupEntity groupEntity = findGroupByUUID(groupId);
        if (!groupEntity.getAdmin().getId().equals(currentUserId)) {
            throw new CustomException(ErrorCodes.ACCESS_DENIED); // Убедитесь, что ошибка корректная
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


    private GroupEntity findGroupByUUID(UUID uuid) {
        return groupRepository.findById(uuid)
                .orElseThrow(() -> new CustomException(ErrorCodes.NEWS_NOT_FOUND));
    }
}
