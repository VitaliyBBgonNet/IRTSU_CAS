package bbgon.irtsu_cas.services.impl;

import bbgon.irtsu_cas.dto.FIO;
import bbgon.irtsu_cas.dto.request.FilterDetailRequest;
import bbgon.irtsu_cas.dto.response.CustomSuccessResponse;
import bbgon.irtsu_cas.dto.response.DetailResponse;
import bbgon.irtsu_cas.dto.response.PageableResponse;
import bbgon.irtsu_cas.dto.response.TableElementResponse;
import bbgon.irtsu_cas.entity.DetailsEntity;
import bbgon.irtsu_cas.entity.QDetailsEntity;
import bbgon.irtsu_cas.repositories.DetailsRepository;
import bbgon.irtsu_cas.services.UserService;
import bbgon.irtsu_cas.util.QPredicates;
import com.querydsl.core.types.Predicate;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class ResourceServiceImpl implements ResourceService {

    private final DetailsRepository detailsRepository;

    private final UserService userService;

    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    public List<String> listOwners() {
        return detailsRepository.findAll().stream().map(
                usersEntity -> {
                    String name = Optional.ofNullable(usersEntity.getOwner())
                            .map(owner -> Optional.ofNullable(owner.getName()).orElse(""))
                            .orElse("");
                    String lastName = Optional.ofNullable(usersEntity.getOwner())
                            .map(owner -> Optional.ofNullable(owner.getLastName()).orElse(""))
                            .orElse("");
                    String surname = Optional.ofNullable(usersEntity.getOwner())
                            .map(owner -> Optional.ofNullable(owner.getSurname()).orElse(""))
                            .orElse("");
                    var fio = name + " " + lastName + " " + surname;
                    return fio;
                }).distinct().toList();
    }

    @Override
    public List<TableElementResponse> listAllElements() {
        return detailsRepository.findAll().stream()
                .map(detailsEntity -> {

                    String ownerFullName = detailsEntity.getOwner() != null
                            ? (detailsEntity.getOwner().getName() != null ? detailsEntity.getOwner().getName() : "") + " " +
                            (detailsEntity.getOwner().getLastName() != null ? detailsEntity.getOwner().getLastName() : "")
                            : "—";
                    String ownerAvatar = detailsEntity.getOwner() != null && detailsEntity.getOwner().getAvatar() != null
                            ? detailsEntity.getOwner().getAvatar()
                            : "";


                    String tenantFullName = detailsEntity.getTenant() != null
                            ? Stream.of(
                                    detailsEntity.getTenant().getName(),
                                    detailsEntity.getTenant().getSurname(),
                                    detailsEntity.getTenant().getLastName()
                            )
                            .filter(Objects::nonNull)
                            .filter(str -> !str.trim().isEmpty())
                            .collect(Collectors.joining(" "))
                            : "-";

                    String documentation = detailsEntity.getDocumentation() != null
                            ? detailsEntity.getDocumentation()
                            : "-";

                    return new TableElementResponse(
                            detailsEntity.getId(),
                            detailsEntity.getName(),
                            detailsEntity.getDescription(),
                            detailsEntity.getStatus(),
                            ownerFullName.trim(), // Убираем лишние пробелы
                            ownerAvatar,
                            tenantFullName,
                            documentation
                    );
                })
                .collect(Collectors.toList());
    }

    public List<TableElementResponse> getDetailWitchPaginationFilterForAuthUser(
            FilterDetailRequest filterDetailRequest){

        UUID uuidAuthUser = userService.getUserIdByToken();

        return getDetailWitchPaginationAndPredicateFilter(
                0,
                10,
                filterDetailRequest.getName(),
                filterDetailRequest.getStatus(),
                null,
                uuidAuthUser);
    }

    @Override
    public List<TableElementResponse> getDetailWitchPaginationAndPredicateFilter(
            Integer page, Integer perPage,
            String detailName,
            String status,
            String ownerFullName, UUID ownerId) {

        QDetailsEntity detailsEntity = QDetailsEntity.detailsEntity;

        FIO fio = new FIO();
        if (ownerFullName != null && !ownerFullName.trim().isEmpty()) {
            fio = splitFullName(ownerFullName);
        }

        Pageable pageable = PageRequest.of(page, perPage);

        Predicate predicate = QPredicates.builder()
                .add(ownerId, detailsEntity.owner.id::eq)
                .add(detailName, detailsEntity.name::containsIgnoreCase)
                .add(status, detailsEntity.status::containsIgnoreCase)
                .add(fio.getName(), detailsEntity.owner.name::containsIgnoreCase)
                .add(fio.getLastname(), detailsEntity.owner.lastName::containsIgnoreCase)
                .add(fio.getSurname(), detailsEntity.owner.surname::containsIgnoreCase)
                .add(detailName, detailsEntity.documentation::containsIgnoreCase) // Добавляем фильтр по документации, если нужно
                .buildAnd();

        Page<DetailsEntity> result = detailsRepository.findAll(predicate, pageable);

        List<TableElementResponse> detailResponseList = result.getContent().stream()
                .map(entity -> {
                    // Безопасное получение данных владельца
                    String ownerFullNameStr = entity.getOwner() != null
                            ? (entity.getOwner().getName() != null ? entity.getOwner().getName() : "") + " " +
                            (entity.getOwner().getLastName() != null ? entity.getOwner().getLastName() : "")
                            : "—";
                    String ownerAvatar = entity.getOwner() != null && entity.getOwner().getAvatar() != null
                            ? entity.getOwner().getAvatar()
                            : "";

                    // Безопасное получение полного ФИО арендатора (Имя Отчество Фамилия)
                    String tenantFullName = entity.getTenant() != null
                            ? Stream.of(
                                    entity.getTenant().getName(),
                                    entity.getTenant().getSurname(),
                                    entity.getTenant().getLastName()
                            )
                            .filter(Objects::nonNull)
                            .filter(str -> !str.trim().isEmpty())
                            .collect(Collectors.joining(" "))
                            : "-";

                    // Безопасное получение документации
                    String documentation = entity.getDocumentation() != null
                            ? entity.getDocumentation()
                            : "-";

                    return new TableElementResponse(
                            entity.getId(),
                            entity.getName(),
                            entity.getDescription(),
                            entity.getStatus(),
                            ownerFullNameStr.trim(), // Убираем лишние пробелы
                            ownerAvatar,
                            tenantFullName,
                            documentation // Добавили документацию в ответ
                    );
                })
                .collect(Collectors.toList());

        return detailResponseList;
    }

    private FIO splitFullName(String fullName) {
        if (fullName == null || fullName.trim().isEmpty()) {
            return new FIO(null, null, null);
        }

        String[] strings = fullName.trim().split("\\s+"); // Разделяем по любому количеству пробелов
        FIO fio;
        switch (strings.length) {
            case 3:
                fio = new FIO(strings[0], strings[1], strings[2]);
                break;
            case 2:
                fio = new FIO(strings[0], null, strings[1]); // Предполагаем, что без отчества
                break;
            case 1:
                fio = new FIO(strings[0], null, null); // Только имя
                break;
            default:
                fio = new FIO(null, null, null); // Если формат некорректен
        }
        return fio;
    }
}
