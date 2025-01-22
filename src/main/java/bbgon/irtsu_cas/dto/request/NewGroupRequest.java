package bbgon.irtsu_cas.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewGroupRequest {
    private String groupName;

    private String description;

    private String logo;
}
