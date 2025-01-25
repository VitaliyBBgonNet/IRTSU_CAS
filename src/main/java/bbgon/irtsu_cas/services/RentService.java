package bbgon.irtsu_cas.services;

import bbgon.irtsu_cas.dto.response.CustomSuccessResponse;
import bbgon.irtsu_cas.dto.response.SuccessResponse;

import java.util.UUID;

public interface RentService {
    CustomSuccessResponse<SuccessResponse> getDetailFromAnyUserWhoAlsoInOneGroup(UUID uuidDetail);
}
