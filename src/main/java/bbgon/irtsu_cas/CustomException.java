package bbgon.irtsu_cas;

import bbgon.irtsu_cas.constants.ErrorCodes;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class CustomException extends RuntimeException {
    private final ErrorCodes errorCodes;
}
