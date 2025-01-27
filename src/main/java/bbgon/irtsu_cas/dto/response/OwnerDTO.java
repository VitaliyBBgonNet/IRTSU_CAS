package bbgon.irtsu_cas.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OwnerDTO {
    private UUID id;

    private String name;
    private String lastName;
    private String surname;
    private String email;
    private String position;
    private String avtar;
    private String phone;
    private String department;
}
