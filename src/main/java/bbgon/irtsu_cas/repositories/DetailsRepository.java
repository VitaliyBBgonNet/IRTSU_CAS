package bbgon.irtsu_cas.repositories;

import bbgon.irtsu_cas.entity.DetailsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.UUID;
import java.util.function.Predicate;

public interface DetailsRepository extends JpaRepository<DetailsEntity, UUID>, QuerydslPredicateExecutor<DetailsEntity> {
}
