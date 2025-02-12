package bbgon.irtsu_cas.repositories;

import bbgon.irtsu_cas.dto.response.projections.StatusProjection;
import bbgon.irtsu_cas.entity.DetailsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;

public interface DetailsRepository extends JpaRepository<DetailsEntity, UUID>, QuerydslPredicateExecutor<DetailsEntity> {
    @Query("select distinct d.status from DetailsEntity d")
    Optional<List<String>>getAllStatus();
}
