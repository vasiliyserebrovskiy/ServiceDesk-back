package com.sitool.servicedesk.incident.mapper;

import com.sitool.servicedesk.incident.dto.response.IncidentDto;
import com.sitool.servicedesk.incident.entity.Incident;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface IncidentMapper {
    @Mapping(target = "requesterId", expression = "java(incident.getRequester().getId())")
    @Mapping(target = "categoryId", expression = "java(incident.getCategory().getId())")
    @Mapping(target = "subcategoryId", expression = "java(incident.getSubcategory() != null ? incident.getSubcategory().getId() : null)")
    @Mapping(target = "statusId", expression = "java(incident.getStatus().getId())")
    @Mapping(target = "ciId", expression = "java(incident.getCi() != null ? incident.getCi().getId() : null)")
    @Mapping(target = "groupId", expression = "java(incident.getGroup() != null ? incident.getGroup().getId() : null)")
    @Mapping(target = "assigneeId", expression = "java(incident.getAssignee() != null ? incident.getAssignee().getId() : null)")
    IncidentDto toIncidentDto(Incident incident);
}
