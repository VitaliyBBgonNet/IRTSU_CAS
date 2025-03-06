package bbgon.irtsu_cas.services;

import bbgon.irtsu_cas.dto.request.DetailProperties;
import bbgon.irtsu_cas.dto.request.UpdateDetailProperties;
import bbgon.irtsu_cas.dto.response.*;

import java.util.List;
import java.util.UUID;

public interface DetailsService {

    CustomSuccessResponse<SuccessResponse> updatedDetail(UpdateDetailProperties updateDetail);

    CustomSuccessResponse<SuccessResponse> deleteElementById(String id);

    List<TableElementResponse> getDetailsForAuthUser();

    CustomSuccessResponse<String> createDetail(DetailProperties detailProperties);

    CustomSuccessResponse<PageableResponse<List<DetailResponse>>> getDetailsPagination(Integer page, Integer perPage);

    CustomSuccessResponse<String> deleteListDetails(List<UUID> uuidList);

    CustomSuccessResponse<PageableResponse<List<DetailResponse>>> getDetailWitchPaginationAndPredicateFilter(
            Integer page, Integer perPage,
            String detailName,
            String status,
            UUID ownerId,
            UUID orderHumanId,
            UUID groupId,
            String dataAdd);
}
