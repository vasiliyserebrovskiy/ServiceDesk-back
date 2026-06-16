package com.sitool.servicedesk.incident.controller;

import com.sitool.servicedesk.incident.dto.request.CreateIncidentRequest;
import com.sitool.servicedesk.incident.dto.request.UpdateIncidentRequest;
import com.sitool.servicedesk.incident.dto.response.IncidentDto;
import com.sitool.servicedesk.incident.dto.response.NextIncidentNumberResponse;
import com.sitool.servicedesk.incident.service.IncidentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

/**
 * REST controller that delegates incident operations to the IncidentService.
 */
@RestController
@RequiredArgsConstructor
public class IncidentController implements IncidentApi {

    private final IncidentService incidentService;

    @Override
    public NextIncidentNumberResponse getNextIncidentNumber() {
        return incidentService.getNextIncidentNumber();
    }

    @Override
    public IncidentDto createIncident(CreateIncidentRequest request) {
        return incidentService.createIncident(request);
    }

    @Override
    public IncidentDto updateIncident(UUID incidentId, UpdateIncidentRequest request) {
        return incidentService.updateIncident(incidentId, request);
    }

    @Override
    public IncidentDto getIncident(UUID incidentId) {
        return incidentService.getIncident(incidentId);
    }

    @Override
    public List<IncidentDto> getAllIncidents() {
        return incidentService.getAllIncidents();
    }
}
