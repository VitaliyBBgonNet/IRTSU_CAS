package bbgon.irtsu_cas.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UpdateDetailProperties {

    String id;

    @NotBlank(message = "DETAIL_NAME_NOT_BE_NULL")
    String name;

    @NotBlank(message = "DETAIL_DESCRIPTION_NOT_BE_NULL")
    String description;

    @NotBlank(message = "DETAIL_STATUS_NOT_BE_NULL")
    String status;

    String documentation;

    String tenant;

    @Override
    public String toString() {
        return id + " " + name + " " + description + " " + status + " " + documentation + " " + tenant + "\n";
    }
}
