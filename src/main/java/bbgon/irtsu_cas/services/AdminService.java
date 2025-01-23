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
}
