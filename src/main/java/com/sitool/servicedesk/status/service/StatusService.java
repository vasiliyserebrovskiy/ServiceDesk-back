package com.sitool.servicedesk.status.service;

import com.sitool.servicedesk.status.dto.request.CreateStatusRequest;
import com.sitool.servicedesk.status.dto.request.UpdateStatusRequest;
import com.sitool.servicedesk.status.dto.response.StatusDto;

import java.util.List;
import java.util.UUID;

public interface StatusService {

    StatusDto createStatus(CreateStatusRequest request);

    StatusDto updateStatus(UUID statusId, UpdateStatusRequest request);

    void deleteStatus(UUID statusId);

    StatusDto getStatus(UUID statusId);

    List<StatusDto> getAllStatuses(String type);
}
