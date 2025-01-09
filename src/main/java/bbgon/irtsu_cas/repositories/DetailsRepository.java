package bbgon.irtsu_cas.repositories;

import bbgon.irtsu_cas.entity.DetailsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;
public interface DetailsRepository extends JpaRepository<DetailsEntity, UUID> {
}
