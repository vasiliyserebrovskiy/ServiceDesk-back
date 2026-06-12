package com.sitool.servicedesk.ci.mapper;

import com.sitool.servicedesk.ci.dto.response.CIDto;
import com.sitool.servicedesk.ci.entity.CI;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CIMapper {
    CIDto ciToCIDto(CI ci);
}
