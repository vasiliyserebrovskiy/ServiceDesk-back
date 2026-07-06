package com.sitool.servicedesk.servicenow.settings.mapper;

import com.sitool.servicedesk.servicenow.settings.dto.response.ServiceNowSettingsDto;
import com.sitool.servicedesk.servicenow.settings.entity.ServiceNowSettings;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ServiceNowSettingsMapper {

    @Mapping(target = "passwordConfigured",
            expression = "java(entity.getPassword() != null && !entity.getPassword().isBlank())")
    ServiceNowSettingsDto toDto(ServiceNowSettings entity);

}
