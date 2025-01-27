package bbgon.irtsu_cas.mappers;

import bbgon.irtsu_cas.dto.response.DetailResponse;
import bbgon.irtsu_cas.entity.DetailsEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface DetailMapper {

    DetailResponse toResponse(DetailsEntity detail);
}
