package bbgon.irtsu_cas.services.impl;

import bbgon.irtsu_cas.CustomException;
import bbgon.irtsu_cas.constants.ErrorCodes;
import bbgon.irtsu_cas.dto.CustomDoneRegistrationDTO;
import bbgon.irtsu_cas.dto.UserRegistrationDTO;
import bbgon.irtsu_cas.entity.UsersEntity;
import bbgon.irtsu_cas.mappers.UserMappers;
import bbgon.irtsu_cas.repositories.UserRepository;
import bbgon.irtsu_cas.security.CustomUserDetails;
import bbgon.irtsu_cas.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UsersEntity findUserEntityById(UUID id) {
        return getUserOrThrowException(id);
    }

    @Override
    public UUID getUserIdByToken() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return UUID.fromString(((CustomUserDetails) authentication.getPrincipal()).getUsername());
    }

    private UsersEntity getUserOrThrowException(UUID uuid) {
        UsersEntity usersEntity = userRepository.findById(uuid)
                .orElseThrow(() -> new CustomException(ErrorCodes.USER_NOT_FOUND));
        return usersEntity;
    }
}
