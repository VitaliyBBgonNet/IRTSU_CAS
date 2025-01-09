package bbgon.irtsu_cas.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class OwnerDTO {
    private UUID id;

    private String name;
    private String lastName;
    private String surname;
    private String email;
    private String password;
    private String position;
    private String avtar;
    private String phone;
}
