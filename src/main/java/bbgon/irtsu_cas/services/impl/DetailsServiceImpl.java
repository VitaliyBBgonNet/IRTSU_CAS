package bbgon.irtsu_cas.services.impl;

import bbgon.irtsu_cas.dto.request.DetailProperties;
import bbgon.irtsu_cas.dto.response.CustomSuccessResponse;
import bbgon.irtsu_cas.dto.response.DetailResponse;
import bbgon.irtsu_cas.dto.response.OwnerDTO;
import bbgon.irtsu_cas.dto.response.PageableResponse;
import bbgon.irtsu_cas.entity.DetailsEntity;
import bbgon.irtsu_cas.repositories.DetailsRepository;
import bbgon.irtsu_cas.repositories.UserRepository;
import bbgon.irtsu_cas.services.DetailsService;
import bbgon.irtsu_cas.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

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
        detailsEntity.setImage(detailProperties.getImage());
        detailsRepository.save(detailsEntity);
        return new CustomSuccessResponse<>("DETAIL ADD");
    }

    @Override
    public CustomSuccessResponse<PageableResponse<List<DetailResponse>>> getDetailsPagination(Integer page, Integer perPage) {

        PageRequest pageRequest = PageRequest.of(page - 1, perPage);
        Page<DetailsEntity> detailsEntityPage = detailsRepository.findAll(pageRequest);

        return new CustomSuccessResponse<>(new PageableResponse<>(mapDetails(detailsEntityPage), detailsEntityPage.getTotalElements()));
    }

    @Override
    public CustomSuccessResponse<String> deleteListDetails(List<UUID> uuidList) {
        uuidList.forEach(detailsRepository::deleteById);
        return new CustomSuccessResponse<>("DETAIL DELETE");
    }


    public List<DetailResponse> mapDetails(Page<DetailsEntity> detailsEntityPage) {

        return detailsEntityPage.stream().map(
                detailsEntity -> {

                    OwnerDTO ownerDTO = new OwnerDTO();
                    ownerDTO.setId(detailsEntity.getOwner().getId());
                    ownerDTO.setName(detailsEntity.getOwner().getName());
                    ownerDTO.setLastName(detailsEntity.getOwner().getLastName());
                    ownerDTO.setSurname(detailsEntity.getOwner().getSurname());
                    ownerDTO.setAvtar(detailsEntity.getOwner().getName());
                    ownerDTO.setEmail(detailsEntity.getOwner().getEmail());
                    ownerDTO.setPhone(detailsEntity.getOwner().getPhone());

                    DetailResponse response = new DetailResponse();
                    response.setId(detailsEntity.getId());
                    response.setName(detailsEntity.getName());
                    response.setStatus(detailsEntity.getStatus());
                    response.setOwner(ownerDTO);
                    response.setDescription(detailsEntity.getDescription());
                    response.setCreatedDetail(detailsEntity.getCreatedDetail().toString());
                    response.setDocumentation(detailsEntity.getDocumentation());

                    return response;
                }
        ).toList();
    }
}
