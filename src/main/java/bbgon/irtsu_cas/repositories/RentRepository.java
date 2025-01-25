package bbgon.irtsu_cas.repositories;

import bbgon.irtsu_cas.entity.DetailsEntity;
import bbgon.irtsu_cas.entity.RentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RentRepository extends JpaRepository<RentEntity, Integer> {
    List<RentEntity> findByDetailRent(DetailsEntity detailRent);

    @Query("SELECT COUNT(rent) > 0 FROM RentEntity rent WHERE rent.detailRent.id = :detailId")
    Boolean existsByDetailRent(@Param("detailId") UUID detailId);
}
