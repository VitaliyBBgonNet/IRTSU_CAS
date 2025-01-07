package bbgon.irtsu_cas.services;

import bbgon.irtsu_cas.CustomException;
import bbgon.irtsu_cas.dto.request.RegistrationUserRequest;
import bbgon.irtsu_cas.dto.response.CustomSuccessResponse;
import bbgon.irtsu_cas.dto.response.LoginUserResponse;
import bbgon.irtsu_cas.entity.UsersEntity;
import bbgon.irtsu_cas.repositories.AuthRepository;
import bbgon.irtsu_cas.security.TokenSecurity;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthRepository authRepository;

    private final PasswordEncoder passwordEncoder;

    private final TokenSecurity jwtToken;

    @Override
    public CustomSuccessResponse<LoginUserResponse> registrationUser(RegistrationUserRequest requestForRegistration) {

        authRepository.findByEmail(requestForRegistration.getEmail()).ifPresent(auth -> {
            throw new CustomException("USER_ALREADY_EXISTS");
        });

        String encryptedPassword = passwordEncoder.encode(requestForRegistration.getPassword());

        //Потом переделать через маппер
        UsersEntity usersEntity = new UsersEntity();
        usersEntity.setEmail(requestForRegistration.getEmail());
        usersEntity.setPassword(encryptedPassword);

        authRepository.save(usersEntity);

        //Потом переделать через маппер
        LoginUserResponse loginUserResponse = new LoginUserResponse();
        loginUserResponse.setEmail(requestForRegistration.getEmail());
        loginUserResponse.setFIO(requestForRegistration
                .getLastName()+requestForRegistration
                .getName()+requestForRegistration
                .getSurname());
        loginUserResponse.setToken(jwtToken.generateToken(usersEntity.getId()));

        return new CustomSuccessResponse<>(loginUserResponse);
    }
}
