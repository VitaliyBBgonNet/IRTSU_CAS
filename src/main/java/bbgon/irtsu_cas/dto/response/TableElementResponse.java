package bbgon.irtsu_cas.dto.response;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TableElementResponse {
    private UUID id;

    private String name;

    private String description;

    private String status;

    private String owner;

    private String avatar;

    private String tenant;

    private String documentation;
}
