package bbgon.irtsu_cas.security;
import bbgon.irtsu_cas.CustomException;
import bbgon.irtsu_cas.repositories.AuthRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final AuthRepository authRepository;

    @Override
    public CustomUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        if (authRepository.existsById(UUID.fromString(username))) {
            return new CustomUserDetails(username);
        } else {
            throw new CustomException("USER_NOT_FOUND");
        }
    }
}