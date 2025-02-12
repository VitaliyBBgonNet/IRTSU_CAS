package bbgon.irtsu_cas.services.impl;

import bbgon.irtsu_cas.dto.response.TableElementResponse;

import java.util.List;

public interface ResourceService {
    List<String> listOwners();
    List<TableElementResponse> listAllElements();
}
