package bbgon.irtsu_cas.services.impl;

import bbgon.irtsu_cas.CustomException;
import bbgon.irtsu_cas.constants.ErrorCodes;
import bbgon.irtsu_cas.dto.response.CustomSuccessResponse;
import bbgon.irtsu_cas.dto.response.SuccessResponse;
import bbgon.irtsu_cas.entity.DetailsEntity;
import bbgon.irtsu_cas.entity.RentEntity;
import bbgon.irtsu_cas.entity.UsersEntity;
import bbgon.irtsu_cas.repositories.DetailsRepository;
import bbgon.irtsu_cas.repositories.GroupRepository;
import bbgon.irtsu_cas.repositories.RentRepository;
import bbgon.irtsu_cas.repositories.UserRepository;
import bbgon.irtsu_cas.services.RentService;
import bbgon.irtsu_cas.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RentServiceImpl implements RentService {

    private final RentRepository rentRepository;
    private final UserRepository userRepository;
    private final DetailsRepository detailsRepository;
    private final GroupRepository groupRepository;
    private final UserService userService;

    @Override
    public CustomSuccessResponse<SuccessResponse> getDetailFromAnyUserWhoAlsoInOneGroup(UUID uuidDetail) {

        //Деталь которая берётся в аренду
        DetailsEntity detailsEntity = detailsRepository.findById(uuidDetail)
                .orElseThrow(() -> new CustomException(ErrorCodes.DETAIL_NOT_FOUND));

        if (rentRepository.existsByDetailRent(uuidDetail)){
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

        rentRepository.save(rentEntity);

        return new CustomSuccessResponse<>(new SuccessResponse("Details rented successfully"));
    }
}
