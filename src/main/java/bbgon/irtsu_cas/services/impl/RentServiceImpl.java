package bbgon.irtsu_cas.services.impl;

import bbgon.irtsu_cas.CustomException;
import bbgon.irtsu_cas.constants.ErrorCodes;
import bbgon.irtsu_cas.dto.response.CustomSuccessResponse;
import bbgon.irtsu_cas.dto.response.DetailResponse;
import bbgon.irtsu_cas.entity.DetailsEntity;
import bbgon.irtsu_cas.entity.RentEntity;
import bbgon.irtsu_cas.entity.UsersEntity;
import bbgon.irtsu_cas.repositories.DetailsRepository;
import bbgon.irtsu_cas.repositories.GroupRepository;
import bbgon.irtsu_cas.repositories.RentRepository;
import bbgon.irtsu_cas.repositories.UserRepository;
import bbgon.irtsu_cas.services.RentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RentServiceImpl implements RentService {

    private final RentRepository rentRepository;
    private final UserRepository userRepository;
    private final DetailsRepository detailsRepository;
    private final GroupRepository groupRepository;

    @Override
    public CustomSuccessResponse<String> getDetailFromAnyUserWhoAlsoInOneGroup(UUID uuidDetail, UUID userRent) {

        DetailsEntity detailsEntity = detailsRepository.findById(uuidDetail)
                .orElseThrow(() -> new CustomException(ErrorCodes.DETAIL_NOT_FOUND));

        UsersEntity userWhoRent = userRepository.findById(userRent)
                .orElseThrow(() -> new CustomException(ErrorCodes.USER_NOT_FOUND));

        UsersEntity usersEntity = detailsEntity.getGroup().getUsers().stream()
                .filter(user -> user.getId().equals(userWhoRent.getId()))
                .findFirst().orElseThrow(() -> new CustomException(ErrorCodes.ACCESS_DENIED));

        RentEntity rentEntity = new RentEntity();
        rentEntity.setStartDate(LocalDateTime.now());
        rentEntity.setDetailRent(detailsEntity);
        rentEntity.setUserRent(userWhoRent);

        rentRepository.save(rentEntity);

        return null;
    }
}
