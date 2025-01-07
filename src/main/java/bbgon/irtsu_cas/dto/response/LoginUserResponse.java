package bbgon.irtsu_cas.dto.response;

import lombok.Data;

@Data
public class LoginUserResponse {

    private String email;

    private String id;

    private String FIO;

    private String token;
}
