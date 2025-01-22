package bbgon.irtsu_cas.repositories;

import bbgon.irtsu_cas.entity.GroupEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface GroupRepository extends CrudRepository<GroupEntity, UUID> {
    Optional<GroupEntity> findByName(String groupName);
}
