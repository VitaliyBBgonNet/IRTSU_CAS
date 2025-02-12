package bbgon.irtsu_cas.services.impl;

import bbgon.irtsu_cas.dto.response.TableElementResponse;
import bbgon.irtsu_cas.repositories.DetailsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ResourceServiceImpl implements ResourceService {

    private final DetailsRepository detailsRepository;

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
                            detailsEntity.getOwner().getAvatar());
                }).toList();
    }


}
