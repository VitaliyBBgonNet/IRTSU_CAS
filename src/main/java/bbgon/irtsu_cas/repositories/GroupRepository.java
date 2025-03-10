package bbgon.irtsu_cas.repositories;

import bbgon.irtsu_cas.entity.GroupEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface GroupRepository extends CrudRepository<GroupEntity, UUID> {
    Optional<GroupEntity> findByName(String groupName);

    Optional<Object> findByNameAndId(String name, UUID id);

    List<GroupEntity> findByAdmin_Id(UUID adminId);

    @Query("select distinct g.name from GroupEntity g")
    Optional<List<String>> getAllGroupNames();
}
