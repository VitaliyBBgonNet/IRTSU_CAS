package bbgon.irtsu_cas.services;

import bbgon.irtsu_cas.dto.request.DetailProperties;
import bbgon.irtsu_cas.dto.response.CustomSuccessResponse;

public interface DetailsService {
    CustomSuccessResponse<String> createDetail(DetailProperties detailProperties);

}
