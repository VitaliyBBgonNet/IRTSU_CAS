package bbgon.irtsu_cas.services.impl;

import bbgon.irtsu_cas.CustomException;
import bbgon.irtsu_cas.constants.ErrorCodes;
import bbgon.irtsu_cas.dto.request.DetailProperties;
import bbgon.irtsu_cas.dto.response.*;
import bbgon.irtsu_cas.entity.DetailsEntity;
import bbgon.irtsu_cas.entity.QDetailsEntity;
import bbgon.irtsu_cas.entity.UsersEntity;
import bbgon.irtsu_cas.mappers.DetailMapper;
import bbgon.irtsu_cas.repositories.DetailsRepository;
import bbgon.irtsu_cas.repositories.UserRepository;
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
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
public class DetailsServiceImpl implements DetailsService {

    private final DetailsRepository detailsRepository;

    private final UserService userService;

    private final DetailMapper detailMapper;

    @PersistenceContext
    private final EntityManager entityManager;
    private final UserRepository userRepository;

    @Override
    public CustomSuccessResponse<SuccessResponse> deleteElementById(String idElement) {

        UUID uuidAuthUser = userService.getUserIdByToken();

        Optional<DetailsEntity> detailsEntityOptional = detailsRepository.findById(UUID.fromString(idElement));
        if (detailsEntityOptional.isPresent()) {
            DetailsEntity detailsEntity = detailsEntityOptional.get();
            if (detailsEntity.getOwner().getId().equals(uuidAuthUser)) {
                detailsRepository.delete(detailsEntity);
            }
        }
        return new CustomSuccessResponse<>(new SuccessResponse());
    }

    @Override
    public List<TableElementResponse> getDetailsForAuthUser() {
        UUID uuidAuthUser = userService.getUserIdByToken();
        if (uuidAuthUser == null) {
            return List.of();
        }

        return detailsRepository.findAll().stream()
                .filter(detailsEntity -> {
                    UsersEntity owner = detailsEntity.getOwner();
                    return owner != null && owner.getId() != null && owner.getId().equals(uuidAuthUser);
                })
                .map(detailsEntity -> {
                    String ownerFullName = (detailsEntity.getOwner().getName() != null ? detailsEntity.getOwner().getName() : "") + " " +
                            (detailsEntity.getOwner().getLastName() != null ? detailsEntity.getOwner().getLastName() : "");
                    String ownerAvatar = detailsEntity.getOwner().getAvatar() != null
                            ? detailsEntity.getOwner().getAvatar()
                            : "";

                    String tenantFullName = detailsEntity.getTenant() != null
                            ? Stream.of(
                                    detailsEntity.getTenant().getLastName(),
                                    detailsEntity.getTenant().getName(),
                                    detailsEntity.getTenant().getSurname()
                            )
                            .filter(Objects::nonNull)
                            .filter(str -> !str.trim().isEmpty())
                            .collect(Collectors.joining(" "))
                            : "-";

                    return new TableElementResponse(
                            detailsEntity.getId(),
                            detailsEntity.getName(),
                            detailsEntity.getDescription(),
                            detailsEntity.getStatus(),
                            ownerFullName.trim(),
                            ownerAvatar,
                            tenantFullName
                    );
                })
                .collect(Collectors.toList());
    }

    @Override
    public CustomSuccessResponse<String> createDetail(DetailProperties detailProperties) {

        Optional<UsersEntity> tenantEntity = userRepository.findById(UUID.fromString(detailProperties.getTenant()));
        tenantEntity.orElseThrow(() -> new CustomException(ErrorCodes.USER_NOT_FOUND));

        DetailsEntity detailsEntity = new DetailsEntity();
        detailsEntity.setCreatedDetail(LocalDateTime.now());
        detailsEntity.setDescription(detailProperties.getDescription());
        detailsEntity.setName(detailProperties.getName());
        detailsEntity.setOwner(userService.findUserEntityById(userService.getUserIdByToken()));
        detailsEntity.setDocumentation(detailProperties.getDocumentation());
        detailsEntity.setStatus(detailProperties.getStatus());
        detailsEntity.setImage(detailProperties.getImage());
        detailsEntity.setTenant(tenantEntity.get());
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
