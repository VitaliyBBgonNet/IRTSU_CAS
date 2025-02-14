package bbgon.irtsu_cas.services;

import bbgon.irtsu_cas.dto.request.DetailProperties;
import bbgon.irtsu_cas.dto.request.FilterDetailRequest;
import bbgon.irtsu_cas.dto.response.CustomSuccessResponse;
import bbgon.irtsu_cas.dto.response.DetailResponse;
import bbgon.irtsu_cas.dto.response.PageableResponse;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface DetailsService {
    CustomSuccessResponse<String> createDetail(DetailProperties detailProperties);

    CustomSuccessResponse<String> addNewDetails(
            String componentName,
            String componentStatus,
            String componentDocumentation,
            String componentDescription);

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
