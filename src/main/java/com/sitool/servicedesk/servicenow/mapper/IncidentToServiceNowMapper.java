package com.sitool.servicedesk.servicenow.mapper;

import com.sitool.servicedesk.incident.entity.Incident;
import com.sitool.servicedesk.servicenow.dto.request.ServiceNowIncidentRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface IncidentToServiceNowMapper {

    @Mapping(target = "shortDescription", source = "shortDescription")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "category", expression = "java(incident.getCategory().getName())")
    @Mapping(target = "subcategory",
            expression = "java(incident.getSubcategory() != null ? incident.getSubcategory().getName() : null)")
    @Mapping(target = "impact", expression = "java(incident.getImpact().name())")
    @Mapping(target = "urgency", expression = "java(incident.getUrgency().name())")
    @Mapping(target = "externalNumber", source = "number")
    @Mapping(target = "requesterEmail", expression = "java(incident.getRequester().getEmail())")
    @Mapping(target = "assignmentGroup",
            expression = "java(incident.getGroup() != null ? incident.getGroup().getName() : null)")
    @Mapping(target = "assignedToEmail",
            expression = "java(incident.getAssignee() != null ? incident.getAssignee().getEmail() : null)")
    @Mapping(target = "ciSerialNumber",
            expression = "java(incident.getCi() != null ? incident.getCi().getSerialNumber() : null)")
    @Mapping(target = "ciName",
            expression = "java(incident.getCi() != null ? incident.getCi().getName() : null)")
    ServiceNowIncidentRequest toServiceNowRequest(Incident incident);
}