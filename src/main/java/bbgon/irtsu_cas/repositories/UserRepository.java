package bbgon.irtsu_cas.repositories;

import bbgon.irtsu_cas.entity.UsersEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<UsersEntity, UUID> {
    Optional<UsersEntity> findUsersByEmail(String username);
    Boolean existsByEmail(String email);

    List<UsersEntity> id(UUID id);
}
