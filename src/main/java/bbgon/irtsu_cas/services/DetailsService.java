package bbgon.irtsu_cas.services;

import bbgon.irtsu_cas.dto.request.DetailProperties;
import bbgon.irtsu_cas.dto.response.CustomSuccessResponse;
import bbgon.irtsu_cas.dto.response.DetailResponse;
import bbgon.irtsu_cas.dto.response.PageableResponse;

import java.util.List;
import java.util.UUID;

public interface DetailsService {
    CustomSuccessResponse<String> createDetail(DetailProperties detailProperties);

    CustomSuccessResponse<PageableResponse<List<DetailResponse>>> getDetailsPagination(Integer page, Integer perPage);

    CustomSuccessResponse<String> deleteListDetails(List<UUID> uuidList);
}
