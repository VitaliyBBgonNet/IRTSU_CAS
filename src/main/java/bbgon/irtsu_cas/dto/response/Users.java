package bbgon.irtsu_cas.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Users {

    private String id;

    private String FIO;

    private String email;

    private String password;

    private String phone;

    private String addInformation;
}
