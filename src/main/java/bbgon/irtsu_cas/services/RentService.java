package bbgon.irtsu_cas.services;

import bbgon.irtsu_cas.dto.response.CustomSuccessResponse;
import java.util.UUID;

public interface RentService {
    CustomSuccessResponse<String> getDetailFromAnyUserWhoAlsoInOneGroup(UUID uuidDetail, UUID userRent);
}
