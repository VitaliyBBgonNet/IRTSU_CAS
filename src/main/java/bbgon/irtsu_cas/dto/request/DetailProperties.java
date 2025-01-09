package bbgon.irtsu_cas.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class DetailProperties {

    @NotBlank(message = "DETAIL_NAME_NOT_BE_NULL")
    String name;

    @NotBlank(message = "DETAIL_DESCRIPTION_NOT_BE_NULL")
    String description;

    @NotBlank(message = "DETAIL_STATUS_NOT_BE_NULL")
    String status;

    String documentation;

    String image;
}
