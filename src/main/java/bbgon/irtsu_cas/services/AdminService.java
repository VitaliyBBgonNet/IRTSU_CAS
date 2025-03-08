package bbgon.irtsu_cas.services;

import bbgon.irtsu_cas.dto.request.AddNewUserFromAdmin;
import bbgon.irtsu_cas.dto.request.GroupEditDataRequest;
import bbgon.irtsu_cas.dto.request.NewGroupRequest;
import bbgon.irtsu_cas.dto.request.UpdateUser;
import bbgon.irtsu_cas.dto.response.CustomSuccessResponse;
import bbgon.irtsu_cas.dto.response.SuccessResponse;
import bbgon.irtsu_cas.dto.response.Users;

import java.util.List;
import java.util.UUID;

public interface AdminService {

    CustomSuccessResponse<SuccessResponse> updateUserData(UpdateUser updateUser);

    CustomSuccessResponse<SuccessResponse> createNewUser(AddNewUserFromAdmin addNewUserFromAdmin);

    List<Users> getAllUsersForAdmin();

    CustomSuccessResponse<SuccessResponse> deleteUserById(String id);
}
