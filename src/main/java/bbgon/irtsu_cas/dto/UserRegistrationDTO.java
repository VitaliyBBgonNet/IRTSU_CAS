package bbgon.irtsu_cas.dto;

import lombok.Data;

@Data
public class UserRegistrationDTO {

    private String name;
    private String lastName;
    private String surname;
    private String password;
    private String email;
}
