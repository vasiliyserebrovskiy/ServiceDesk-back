package com.sitool.servicedesk.servicenow.inbound.controller;

import com.sitool.servicedesk.servicenow.inbound.dto.ServiceNowIncidentSyncRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * API contract for ServiceNow Incident synchronization.
 */
@Tag(name = "ServiceNow Incident synchronization", description = "Operations related to ServiceNow Incident synchronization")
@RequestMapping("/api/v1/incidents")
public interface ServiceNowIncidentSyncApi {

    @Operation(
            summary = "Synchronize incident update from ServiceNow",
            description = "Update Incident using ServiceNow synchronization."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Incident updated successfully"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request data"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Incident with given number not found"
            )
    })
    @PutMapping("/{number}/servicenow-sync")
    ResponseEntity<Void> syncIncidentUpdate(@PathVariable String number, @Valid @RequestBody ServiceNowIncidentSyncRequest request);
}
