package bbgon.irtsu_cas.services;

import bbgon.irtsu_cas.dto.request.AuthUserRequest;
import bbgon.irtsu_cas.dto.request.RegistrationUserRequest;
import bbgon.irtsu_cas.dto.response.CustomSuccessResponse;
import bbgon.irtsu_cas.dto.response.LoginUserResponse;

public interface AuthService {
    CustomSuccessResponse<LoginUserResponse> registrationUser(RegistrationUserRequest requestForRegistration);

    CustomSuccessResponse<LoginUserResponse> authorizationUser(AuthUserRequest requestForAuthorization);
}
