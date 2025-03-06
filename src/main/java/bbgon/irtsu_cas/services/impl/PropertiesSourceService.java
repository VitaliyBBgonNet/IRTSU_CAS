package bbgon.irtsu_cas.services.impl;

import bbgon.irtsu_cas.dto.response.PropertiesResponse;
import bbgon.irtsu_cas.dto.response.UserListResponse;
import bbgon.irtsu_cas.repositories.DetailsRepository;
import bbgon.irtsu_cas.repositories.GroupRepository;
import bbgon.irtsu_cas.repositories.UserRepository;
import bbgon.irtsu_cas.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PropertiesSourceService {

    private final UserService userService;
    private final UserRepository userRepository;
    private final DetailsRepository detailsRepository;
    private final GroupRepository groupRepository;

    public PropertiesResponse getProperties() {
        PropertiesResponse response = new PropertiesResponse();
        String[] data = {"Apple", "Banana", "Orange"};
        response.setGroup(data);
        response.setStatus(data);
        response.setOrderStatus(data);
        return response;
    }

    public String getStatus() {

        List<String> properties = detailsRepository.getAllStatus().orElseGet(() -> List.of(""));
        return htmlBuilder(properties);
    }

    public String getOwner() {

        List<String> owners = detailsRepository.findAll().stream().map(
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

        for(String str : owners) {
            System.out.println(str);
        }
        return htmlBuilder(owners);
    }

    public String getGroups() {
        List<String> groupNames = groupRepository.getAllGroupNames().orElseGet(() -> List.of(""));
        return htmlBuilder(groupNames);
    }

    //тут заполняю компонент html и возвращаю его
    private String htmlBuilder(List<String> properties) {

        StringBuilder html = new StringBuilder();

        for (String value : properties) {
            html.append("<option value=\"").append(value).append("\">").append(value).append("</option>");
        }

        return html.toString();
    }

    public List<UserListResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .filter(usersEntity -> usersEntity != null &&
                        usersEntity.getRole() != null &&
                        usersEntity.getRole().equals("Teacher"))
                .map(usersEntity -> {
                    UserListResponse userListResponse = new UserListResponse();
                    userListResponse.setId(String.valueOf(usersEntity.getId()));

                    String lastName = usersEntity.getLastName() != null ? usersEntity.getLastName() : "";
                    String name = usersEntity.getName() != null ? usersEntity.getName() : "";
                    String surname = usersEntity.getSurname() != null ? usersEntity.getSurname() : "";

                    userListResponse.setFio(
                            lastName + " " + name + " " + surname
                    );
                    return userListResponse;
                })
                .toList();
    }
}
