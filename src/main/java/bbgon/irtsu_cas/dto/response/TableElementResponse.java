package bbgon.irtsu_cas.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TableElementResponse {
    private String name;

    private String description;

    private String status;

    private String owner;

    private String avatar;
}
