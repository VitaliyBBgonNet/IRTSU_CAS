package bbgon.irtsu_cas.repositories;

import bbgon.irtsu_cas.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<Users, UUID> {
    Optional<Users> findUsersByEmail(String username);
    Boolean existsByEmail(String email);
}
