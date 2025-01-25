package bbgon.irtsu_cas.services;

import bbgon.irtsu_cas.dto.request.GroupEditDataRequest;
import bbgon.irtsu_cas.dto.request.NewGroupRequest;
import bbgon.irtsu_cas.dto.response.CustomSuccessResponse;
import bbgon.irtsu_cas.dto.response.SuccessResponse;

import java.util.UUID;

public interface AdminService {
    CustomSuccessResponse<SuccessResponse> createNewGroup(NewGroupRequest newGroupRequest);

    CustomSuccessResponse<SuccessResponse> deleteGroup(UUID groupId, String groupName);

    CustomSuccessResponse<SuccessResponse> editGroupProperties(UUID groupId, GroupEditDataRequest groupEditDataRequest);

    CustomSuccessResponse<SuccessResponse> addDetailsInOwnGroup(UUID groupId, UUID detailId);

    CustomSuccessResponse<SuccessResponse> addUserInGroup(UUID uuidUser , UUID uuidGroup);

    CustomSuccessResponse<SuccessResponse> deleteUserFromOwnGroup(UUID uuidUser , UUID uuidGroup);

    CustomSuccessResponse<SuccessResponse> deleteDetailFromOwnGroup(UUID uuidDetail, UUID uuidGroup);
}
