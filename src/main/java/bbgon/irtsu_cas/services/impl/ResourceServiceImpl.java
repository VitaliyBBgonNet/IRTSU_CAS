package bbgon.irtsu_cas.services.impl;

import bbgon.irtsu_cas.dto.FIO;
import bbgon.irtsu_cas.dto.response.TableElementResponse;
import bbgon.irtsu_cas.entity.DetailsEntity;
import bbgon.irtsu_cas.entity.QDetailsEntity;
import bbgon.irtsu_cas.repositories.DetailsRepository;
import bbgon.irtsu_cas.util.QPredicates;
import com.querydsl.core.types.Predicate;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ResourceServiceImpl implements ResourceService {

    private final DetailsRepository detailsRepository;

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
                .map((detailsEntity) -> {
                    return new TableElementResponse(
                            detailsEntity.getName(),
                            detailsEntity.getDescription(),
                            detailsEntity.getStatus(),
                            detailsEntity.getOwner().getName() + " " + detailsEntity.getOwner().getLastName(),
                            detailsEntity.getOwner().getAvatar(),
                            detailsEntity.getTenant());
                }).toList();
    }

    @Override
    public List<TableElementResponse> getDetailWitchPaginationAndPredicateFilter(
            Integer page, Integer perPage,
            String detailName,
            String status,
            String ownerFullName) {

        QDetailsEntity detailsEntity = QDetailsEntity.detailsEntity;

        FIO fio = splitFullName(ownerFullName);

        Pageable pageable = PageRequest.of(page, perPage);

        Predicate predicate = QPredicates.builder()
                .add(detailName , detailsEntity.name::containsIgnoreCase)
                .add(status, detailsEntity.status::containsIgnoreCase)
                .add(fio.getName(), detailsEntity.owner.name::containsIgnoreCase)
                .add(fio.getLastname(), detailsEntity.owner.lastName::containsIgnoreCase)
                .add(fio.getSurname(), detailsEntity.owner.surname::containsIgnoreCase)
                .buildAnd();
        Page<DetailsEntity> result = detailsRepository.findAll(predicate, pageable);

        List<TableElementResponse> detailResponseList = result.getContent().stream()
                .map(entity -> {
                    return new TableElementResponse(
                            entity.getName(),
                            entity.getDescription(),
                            entity.getStatus(),
                            entity.getOwner().getName() + " " + entity.getOwner().getLastName(),
                            entity.getOwner().getAvatar(),
                            entity.getTenant());
                }).toList();

        System.out.println(result.iterator().hasNext());
        return detailResponseList;
    }

    private FIO splitFullName(String fullName) {
        String[] strings = fullName.split(" ");
        FIO fio;
        if (strings.length == 3) {
            fio = new FIO(strings[0], strings[1], strings[2]);
        }else {
            fio = new FIO(null,null,null);
        }
        return fio;
    }
}
