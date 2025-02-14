package bbgon.irtsu_cas.services.impl;

import bbgon.irtsu_cas.dto.request.DetailProperties;
import bbgon.irtsu_cas.dto.response.CustomSuccessResponse;
import bbgon.irtsu_cas.dto.response.DetailResponse;
import bbgon.irtsu_cas.dto.response.OwnerDTO;
import bbgon.irtsu_cas.dto.response.PageableResponse;
import bbgon.irtsu_cas.entity.DetailsEntity;
import bbgon.irtsu_cas.entity.QDetailsEntity;
import bbgon.irtsu_cas.mappers.DetailMapper;
import bbgon.irtsu_cas.repositories.DetailsRepository;
import bbgon.irtsu_cas.services.DetailsService;
import bbgon.irtsu_cas.services.UserService;
import bbgon.irtsu_cas.util.QPredicates;
import com.querydsl.core.types.Predicate;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class DetailsServiceImpl implements DetailsService {

    private final DetailsRepository detailsRepository;

    private final UserService userService;

    private final DetailMapper detailMapper;

    @PersistenceContext
    private final EntityManager entityManager;

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
        detailsEntity.setRent(null);
        detailsRepository.save(detailsEntity);
        return new CustomSuccessResponse<>("DETAIL ADD");
    }

    @Override
    public CustomSuccessResponse<String> addNewDetails(
            String componentName,
            String componentStatus,
            String componentDocumentation,
            String componentDescription) {
        DetailsEntity detailsEntity = new DetailsEntity();
        detailsEntity.setCreatedDetail(LocalDateTime.now());
        detailsEntity.setDescription(componentDescription);
        detailsEntity.setName(componentName);
        detailsEntity.setOwner(userService.findUserEntityById(userService.getUserIdByToken()));
        detailsEntity.setDocumentation(componentDocumentation);
        detailsEntity.setStatus(componentStatus);
        detailsEntity.setRent(null);
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

    @Override
    public CustomSuccessResponse<PageableResponse<List<DetailResponse>>> getDetailWitchPaginationAndPredicateFilter(
            Integer page, Integer perPage,
            String detailName,
            String status,
            UUID ownerId,
            UUID orderHumanId,
            UUID groupId,
            String data) {

        LocalDateTime dataAdd = null;
        if (data != null && !data.isEmpty()) {
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS");
                dataAdd = LocalDateTime.parse(data, formatter);
            } catch (DateTimeParseException e) {// Потом как нибудь корректно обработать
                System.out.println("Ошибка при разборе даты: " + e.getMessage());
            }
        }

        QDetailsEntity detailsEntity = QDetailsEntity.detailsEntity;

        Pageable pageable = PageRequest.of(page, perPage);

        Predicate predicate = QPredicates.builder()
                .add(detailName, detailsEntity.name::containsIgnoreCase)
                .add(status, detailsEntity.status::containsIgnoreCase)
                .add(ownerId, detailsEntity.owner.id::eq)
                .add(orderHumanId, detailsEntity.rent.id::eq)
                .add(groupId, detailsEntity.group.id::eq)
                .add(dataAdd, detailsEntity.createdDetail::eq)
                .buildAnd();

        Page<DetailsEntity> result = detailsRepository.findAll(predicate, pageable);

        List<DetailResponse> detailResponseList = result.getContent().stream()
                .map(entity -> {
                    DetailResponse detailResponse = new DetailResponse();
                    detailResponse = detailMapper.toResponse(entity);
                    return detailResponse;
                }).toList();

        System.out.println(result.iterator().hasNext());

        PageableResponse<List<DetailResponse>> pageableResponse = new PageableResponse<>(
                detailResponseList, result.getTotalElements()
        );

        System.out.println();
        return new CustomSuccessResponse<>(pageableResponse);
    }

    public List<DetailResponse> mapDetails(Page<DetailsEntity> detailsEntityPage) {

        return detailsEntityPage.stream().map(
                detailsEntity -> {

                    OwnerDTO ownerDTO = getOwnerDTO(detailsEntity);

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

    private OwnerDTO getOwnerDTO(DetailsEntity detailsEntity) {
        OwnerDTO ownerDTO = new OwnerDTO();
        ownerDTO.setId(detailsEntity.getOwner().getId());
        ownerDTO.setName(detailsEntity.getOwner().getName());
        ownerDTO.setLastName(detailsEntity.getOwner().getLastName());
        ownerDTO.setSurname(detailsEntity.getOwner().getSurname());
        ownerDTO.setAvtar(detailsEntity.getOwner().getName());
        ownerDTO.setEmail(detailsEntity.getOwner().getEmail());
        ownerDTO.setPhone(detailsEntity.getOwner().getPhone());
        return ownerDTO;
    }
}
