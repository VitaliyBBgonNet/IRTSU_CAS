package bbgon.irtsu_cas.services.impl;

import bbgon.irtsu_cas.CustomException;
import bbgon.irtsu_cas.constants.ErrorCodes;
import bbgon.irtsu_cas.dto.request.NewGroupRequest;
import bbgon.irtsu_cas.dto.response.CustomSuccessResponse;
import bbgon.irtsu_cas.dto.response.SuccessResponse;
import bbgon.irtsu_cas.entity.GroupEntity;
import bbgon.irtsu_cas.entity.UsersEntity;
import bbgon.irtsu_cas.repositories.GroupRepository;
import bbgon.irtsu_cas.repositories.UserRepository;
import bbgon.irtsu_cas.services.AdminService;
import bbgon.irtsu_cas.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final GroupRepository groupRepository;

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
}
