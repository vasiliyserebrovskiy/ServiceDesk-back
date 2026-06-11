package com.sitool.servicedesk.status.controller;


import com.sitool.servicedesk.status.dto.request.CreateStatusRequest;
import com.sitool.servicedesk.status.dto.request.UpdateStatusRequest;
import com.sitool.servicedesk.status.dto.response.StatusDto;
import com.sitool.servicedesk.status.service.StatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

/**
 * REST controller that delegates status operations to the StatusService.
 */
@RestController
@RequiredArgsConstructor
public class StatusController implements StatusApi {

    private final StatusService statusService;

    @Override
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public StatusDto createStatus(CreateStatusRequest request) {
        return statusService.createStatus(request);
    }

    @Override
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public StatusDto updateStatus(UUID statusId, UpdateStatusRequest request) {
        return statusService.updateStatus(statusId, request);
    }

    @Override
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public void deleteStatus(UUID statusId) {
        statusService.deleteStatus(statusId);
    }

    @Override
    public StatusDto getStatus(UUID statusId) {
        return statusService.getStatus(statusId);
    }

    @Override
    public List<StatusDto> getAllStatuses(String type) {
        return statusService.getAllStatuses(type);
    }
}
