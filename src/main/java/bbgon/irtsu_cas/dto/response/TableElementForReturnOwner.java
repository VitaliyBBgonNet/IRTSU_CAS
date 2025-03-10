package bbgon.irtsu_cas.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class TableElementForReturnOwner {
    private UUID id;

    private String name;

    private String description;

    private String moderationStatus;
}
