package bbgon.irtsu_cas.services;

import bbgon.irtsu_cas.dto.CustomDoneRegistrationDTO;
import bbgon.irtsu_cas.dto.UserRegistrationDTO;
import bbgon.irtsu_cas.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public CustomDoneRegistrationDTO registerUser(UserRegistrationDTO dto) {

        if(userRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        System.out.println(dto.getEmail());

        return new CustomDoneRegistrationDTO();
    }

}
