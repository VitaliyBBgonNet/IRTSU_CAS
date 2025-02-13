package bbgon.irtsu_cas.services.impl;

import bbgon.irtsu_cas.dto.response.TableElementResponse;

import java.util.List;
import java.util.UUID;

public interface ResourceService {
    List<String> listOwners();
    List<TableElementResponse> listAllElements();
    List<TableElementResponse> getDetailWitchPaginationAndPredicateFilter(
            Integer page, Integer perPage,
            String detailName,
            String status,
            String ownerFullName);
}
