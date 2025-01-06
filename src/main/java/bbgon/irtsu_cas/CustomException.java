package bbgon.irtsu_cas;

import ch.qos.logback.core.spi.ErrorCodes;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CustomException extends RuntimeException{
    private final String errorCodes;
}
