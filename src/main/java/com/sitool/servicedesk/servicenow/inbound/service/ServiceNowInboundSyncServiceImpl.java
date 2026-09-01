package com.sitool.servicedesk.servicenow.inbound.service;

import com.sitool.servicedesk.category.repository.CategoryRepository;
import com.sitool.servicedesk.ci.repository.CIRepository;
import com.sitool.servicedesk.group.repository.GroupRepository;
import com.sitool.servicedesk.incident.entity.Incident;
import com.sitool.servicedesk.incident.exceptions.IncidentNotFoundException;
import com.sitool.servicedesk.incident.repository.IncidentRepository;
import com.sitool.servicedesk.servicenow.inbound.dto.ServiceNowIncidentSyncRequest;
import com.sitool.servicedesk.shared.enums.Impact;
import com.sitool.servicedesk.shared.enums.Urgency;
import com.sitool.servicedesk.shared.utils.PriorityCalculator;
import com.sitool.servicedesk.status.repository.StatusRepository;
import com.sitool.servicedesk.sybcategory.repository.SubcategoryRepository;
import com.sitool.servicedesk.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ServiceNowInboundSyncServiceImpl implements ServiceNowInboundSyncService {

    private final IncidentRepository incidentRepository;
    private final CategoryRepository categoryRepository;
    private final SubcategoryRepository subcategoryRepository;
    private final GroupRepository groupRepository;
    private final UserRepository userRepository;
    private final CIRepository ciRepository;
    private final StatusRepository statusRepository;

    @Override
    @Transactional
    public void syncIncidentUpdate(String number, ServiceNowIncidentSyncRequest request) {
//        log.info("INCIDENT NUMBER: " + number);
//        log.info("INCIDENT DATA: " + request.toString());

        // First of all we need to find incident
        Incident updatedIncident = incidentRepository.findByNumber(number).orElseThrow(() -> {
            log.info("syncIncidentUpdate: incident not found by number {}", number);
            return new IncidentNotFoundException();
        });

        // Begin to change filed by field
        //short_description
        updatedIncident.setShortDescription(request.shortDescription());

        //description
        updatedIncident.setDescription(request.description());

        //category
        categoryRepository.findIdByName(request.category())
                .ifPresentOrElse(
                        id -> updatedIncident.setCategory(categoryRepository.getReferenceById(id)),
                        () -> log.info("Category not found by name '{}', skipping update for incident {}",
                                request.category(), number)
                );
        //subcategory
        subcategoryRepository.findIdByName(request.subcategory())
                .ifPresentOrElse(
                        id -> updatedIncident.setSubcategory(subcategoryRepository.getReferenceById(id)),
                        () -> log.warn("Subcategory not found by name '{}', skipping update for incident {}",
                                request.subcategory(), number)
                );
        //impact
        try {
            updatedIncident.setImpact(Impact.valueOf(request.impact()));
        } catch (IllegalArgumentException e) {
            log.info("Unknown impact value '{}', skipping update for incident {}", request.impact(), number);
        }

        //urgency
        try {
            updatedIncident.setUrgency(Urgency.valueOf(request.urgency()));
        } catch (IllegalArgumentException e) {
            log.info("Unknown urgency value '{}', skipping update for incident {}", request.urgency(), number);
        }
        //priority
        updatedIncident.setPriority(PriorityCalculator.calculate(updatedIncident.getImpact(), updatedIncident.getUrgency()));

        //assignmentGroup
        groupRepository.findIdByName(request.assignmentGroup())
                .ifPresentOrElse(
                        id -> updatedIncident.setGroup(groupRepository.getReferenceById(id)),
                        () -> log.info("Group not found by name '{}', skipping update for incident {}",
                                request.assignmentGroup(), number)
                );

        //assignedToEmail
        userRepository.findIdByEmail(request.assignedToEmail())
                .ifPresentOrElse(
                        id -> updatedIncident.setAssignee(userRepository.getReferenceById(id)),
                        () -> log.info("User not found by email '{}', skipping assignee update for incident {}",
                                request.assignedToEmail(), number)
                );
        //ciSerialNumber and ciName
        Optional<UUID> ciId = ciRepository.findIdBySerialNumber(request.ciSerialNumber());
        if (ciId.isEmpty()) {
            ciId = ciRepository.findIdByName(request.ciName());
        }

        ciId.ifPresentOrElse(
                id -> updatedIncident.setCi(ciRepository.getReferenceById(id)),
                () -> log.info("Configuration item not found by serial number '{}' or name '{}', skipping update for incident {}",
                        request.ciSerialNumber(), request.ciName(), number)
        );

        //status
        statusRepository.findIdByName(request.status())
                .ifPresentOrElse(
                        id -> updatedIncident.setStatus(statusRepository.getReferenceById(id)),
                        () -> log.info("Status not found by name '{}', skipping status update for incident {}",
                                request.status(), number)
                );
        //closeComment
        updatedIncident.setCloseComment(request.closeComment());
        //actualStart
        updatedIncident.setActualStart(request.actualStart());
        //actualEnd
        updatedIncident.setActualEnd(request.actualEnd());

        incidentRepository.save(updatedIncident);
    }
}
