package bbgon.irtsu_cas.services.impl;

import bbgon.irtsu_cas.dto.request.DetailProperties;
import bbgon.irtsu_cas.dto.response.CustomSuccessResponse;
import bbgon.irtsu_cas.entity.DetailsEntity;
import bbgon.irtsu_cas.repositories.DetailsRepository;
import bbgon.irtsu_cas.services.DetailsService;
import bbgon.irtsu_cas.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class DetailsServiceImpl implements DetailsService {

    private final DetailsRepository detailsRepository;

    private final UserService userService;

    @Override
    public CustomSuccessResponse<String> createDetail(DetailProperties detailProperties) {
        DetailsEntity detailsEntity = new DetailsEntity();
        detailsEntity.setCreatedDetail(LocalDateTime.now());
        detailsEntity.setDescription(detailProperties.getDescription());
        detailsEntity.setName(detailProperties.getName());
        detailsEntity.setOwner(userService.findUserEntityById(userService.getUserIdByToken()));
        detailsEntity.setDocumentation(detailProperties.getDocumentation());
        detailsEntity.setStatus(detailProperties.getStatus());
        detailsRepository.save(detailsEntity);
        return new CustomSuccessResponse<>("DETAIL ADD");
    }
}
