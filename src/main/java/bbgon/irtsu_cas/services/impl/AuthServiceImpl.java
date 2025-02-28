package bbgon.irtsu_cas.services.impl;

import bbgon.irtsu_cas.CustomException;
import bbgon.irtsu_cas.constants.ErrorCodes;
import bbgon.irtsu_cas.dto.request.AuthUserRequest;
import bbgon.irtsu_cas.dto.request.RegistrationUserRequest;
import bbgon.irtsu_cas.dto.response.CustomSuccessResponse;
import bbgon.irtsu_cas.dto.response.LoginUserResponse;
import bbgon.irtsu_cas.entity.UsersEntity;
import bbgon.irtsu_cas.repositories.AuthRepository;
import bbgon.irtsu_cas.security.TokenSecurity;
import bbgon.irtsu_cas.services.AuthService;
import bbgon.irtsu_cas.utils.AESUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthRepository authRepository;

    private final PasswordEncoder passwordEncoder;

    private final TokenSecurity jwtToken;

    private final AESUtil aesUtil;

    @Override
    public CustomSuccessResponse<LoginUserResponse> registrationUser(RegistrationUserRequest requestForRegistration) {

        authRepository.findByEmail(requestForRegistration.getEmail()).ifPresent(auth -> {
            throw new CustomException(ErrorCodes.USER_ALREADY_EXISTS);
        });

        String passwordEncrypt = aesUtil.encrypt(requestForRegistration.getPassword());

        //Потом переделать через маппер
        UsersEntity usersEntity = new UsersEntity();
        usersEntity.setEmail(requestForRegistration.getEmail());
        usersEntity.setPassword(passwordEncrypt);
        usersEntity.setCreatedAccount(LocalDateTime.now());
        usersEntity.setName(requestForRegistration.getName());
        usersEntity.setLastName(requestForRegistration.getLastName());
        usersEntity.setSurname(requestForRegistration.getSurname());
        usersEntity.setPhone(requestForRegistration.getPhone());

        authRepository.save(usersEntity);

        //Потом переделать через маппер
        LoginUserResponse loginUserResponse = new LoginUserResponse();
        loginUserResponse.setToken(jwtToken.generateToken(usersEntity.getId()));

        return new CustomSuccessResponse<>(loginUserResponse);
    }

    @Override
    public CustomSuccessResponse<LoginUserResponse> authorizationUser(AuthUserRequest requestForAuthorization) {

        UsersEntity usersEntity = authRepository.findByEmail(requestForAuthorization.getEmail())
                .orElseThrow(() -> new CustomException(ErrorCodes.USER_NOT_FOUND));

        if (!aesUtil.decrypt(requestForAuthorization.getPassword()).equals(aesUtil.encrypt(usersEntity.getPassword()))) {
            throw new CustomException(ErrorCodes.USER_PASSWORD_NOT_VALID);
        }

        LoginUserResponse loginUserResponse = new LoginUserResponse();
        loginUserResponse.setToken(jwtToken.generateToken(usersEntity.getId()));
        return new CustomSuccessResponse<>(loginUserResponse);
    }
}
