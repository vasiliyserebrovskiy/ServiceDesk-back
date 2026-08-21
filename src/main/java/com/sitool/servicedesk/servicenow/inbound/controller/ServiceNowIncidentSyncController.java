package com.sitool.servicedesk.servicenow.inbound.controller;

import com.sitool.servicedesk.servicenow.inbound.dto.ServiceNowIncidentSyncRequest;
import com.sitool.servicedesk.servicenow.inbound.service.ServiceNowInboundSyncService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ServiceNowIncidentSyncController implements ServiceNowIncidentSyncApi {

    private final ServiceNowInboundSyncService serviceNowInboundSyncService;

    @Override
    public ResponseEntity<Void> syncIncidentUpdate(String number, ServiceNowIncidentSyncRequest request) {
        serviceNowInboundSyncService.syncIncidentUpdate(number, request);
        return ResponseEntity.noContent().build();
    }
}
