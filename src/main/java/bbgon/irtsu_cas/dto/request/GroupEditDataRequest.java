package bbgon.irtsu_cas.dto.request;

import lombok.Data;

@Data
public class GroupEditDataRequest {
    private String groupName;

    private String description;

    private String logo;
}
