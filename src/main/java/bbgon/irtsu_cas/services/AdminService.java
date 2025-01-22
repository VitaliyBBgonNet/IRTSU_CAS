package bbgon.irtsu_cas.services;

import bbgon.irtsu_cas.dto.request.NewGroupRequest;
import bbgon.irtsu_cas.dto.response.CustomSuccessResponse;
import bbgon.irtsu_cas.dto.response.SuccessResponse;

public interface AdminService {
    CustomSuccessResponse<SuccessResponse> createNewGroup(NewGroupRequest newGroupRequest);
}
