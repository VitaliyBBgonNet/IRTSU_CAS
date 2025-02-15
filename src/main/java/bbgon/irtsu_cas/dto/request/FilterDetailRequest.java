package bbgon.irtsu_cas.dto.request;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class FilterDetailRequest {

    String name;

    String status;
}
