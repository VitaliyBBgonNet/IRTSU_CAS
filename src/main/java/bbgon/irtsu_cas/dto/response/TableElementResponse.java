package bbgon.irtsu_cas.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class TableElementResponse {
    private UUID id;

    private String name;

    private String description;

    private String status;

    private String owner;

    private String avatar;

    private String tenant;
}
