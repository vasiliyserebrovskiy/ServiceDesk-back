package com.sitool.servicedesk.status.mapper;

import com.sitool.servicedesk.status.dto.response.StatusDto;
import com.sitool.servicedesk.status.entity.Status;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface StatusMapper {
    StatusDto statusToStatusDto(Status status);
}
