package bbgon.irtsu_cas.dto.response;

import bbgon.irtsu_cas.entity.UsersEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class DetailResponse {
    private UUID id;
    private String name;
    private String status;
    private OwnerDTO owner;
    private String createdDetail;
    private String description;
    private String documentation;
}
