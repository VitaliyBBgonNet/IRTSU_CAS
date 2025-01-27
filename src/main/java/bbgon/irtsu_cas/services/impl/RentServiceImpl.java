package bbgon.irtsu_cas.services.impl;

import bbgon.irtsu_cas.CustomException;
import bbgon.irtsu_cas.constants.ErrorCodes;
import bbgon.irtsu_cas.dto.response.CustomSuccessResponse;
import bbgon.irtsu_cas.dto.response.SuccessResponse;
import bbgon.irtsu_cas.entity.DetailsEntity;
import bbgon.irtsu_cas.entity.QDetailsEntity;
import bbgon.irtsu_cas.entity.RentEntity;
import bbgon.irtsu_cas.entity.UsersEntity;
import bbgon.irtsu_cas.repositories.DetailsRepository;
import bbgon.irtsu_cas.repositories.RentRepository;
import bbgon.irtsu_cas.services.RentService;
import bbgon.irtsu_cas.services.UserService;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class RentServiceImpl implements RentService {

    private final RentRepository rentRepository;
    private final DetailsRepository detailsRepository;
    private final UserService userService;

    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    public CustomSuccessResponse<SuccessResponse> getDetailFromAnyUserWhoAlsoInOneGroup(UUID uuidDetail) {

        //Деталь которая берётся в аренду
        DetailsEntity detailsEntity = detailsRepository.findById(uuidDetail)
                .orElseThrow(() -> new CustomException(ErrorCodes.DETAIL_NOT_FOUND));

        if (rentRepository.existsByDetailRent(uuidDetail)) {
            throw new CustomException(ErrorCodes.DETAIL_RENTED_EARLIER);
        }

        //Сущность пользователя кто берёт в аренду
        UsersEntity usersEntity = detailsEntity.getGroup()
                .getUsers().stream()
                .filter(userInThisGroup -> userInThisGroup.getId()
                        .equals(userService.getUserIdByToken()))
                .findFirst().orElseThrow(() -> new CustomException(ErrorCodes.ACCESS_DENIED));

        RentEntity rentEntity = new RentEntity();
        rentEntity.setStartDate(LocalDateTime.now());
        rentEntity.setDetailRent(detailsEntity);
        rentEntity.setRentalStatus("RENTED");
        rentEntity.setUserRent(usersEntity);
        detailsEntity.setRent(rentEntity);

        rentRepository.save(rentEntity);

        return new CustomSuccessResponse<>(new SuccessResponse("Details rented successfully"));
    }

    private void test() {

        UUID detailID = UUID.fromString("4b3c5595-5634-47ed-816b-2548527b5872");

        UUID groupId = UUID.fromString("eb98daf8-d25c-44f2-8841-fc7da86be748");

        QDetailsEntity qDetailsEntity = QDetailsEntity.detailsEntity;

        List<Predicate> predicates = new ArrayList<>();

        predicates.add(qDetailsEntity.group.id.eq(groupId));
        predicates.add(qDetailsEntity.id.eq(detailID));

        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);

        String detailName = queryFactory
                .select(qDetailsEntity.id)
                .from(qDetailsEntity)
                .where(predicates.toArray(new Predicate[0]))
                .fetchOne().toString();

        System.out.println(detailName);
    }
}
