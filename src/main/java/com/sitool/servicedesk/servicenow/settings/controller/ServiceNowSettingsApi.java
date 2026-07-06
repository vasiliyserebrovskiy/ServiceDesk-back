package com.sitool.servicedesk.servicenow.settings.controller;

import com.sitool.servicedesk.servicenow.settings.dto.request.UpdateServiceNowSettingsRequest;
import com.sitool.servicedesk.servicenow.settings.dto.response.ServiceNowSettingsDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

/**
 * API contract for ServiceNow settings operations.
 */
@Tag(name = "ServiceNow Settings", description = "Operations related to ServiceNow integration settings")
@RequestMapping("/api/v1/servicenow/settings")
public interface ServiceNowSettingsApi {

    @Operation(
            summary = "Update ServiceNow Settings",
            description = "Updates editable information for ServiceNow Settings."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "ServiceNow settings updated successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ServiceNowSettingsDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request data"
            ),
    })
    @PutMapping()
    ServiceNowSettingsDto updateServiceNowSettings(@Valid @RequestBody UpdateServiceNowSettingsRequest request);

    @Operation(
            summary = "Get ServiceNow settings.",
            description = "Returns ServiceNow settings information."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "ServiceNow settings retrieved successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ServiceNowSettingsDto.class)
                    )
            ),
    })
    @GetMapping()
    ServiceNowSettingsDto getServiceNowSettings();
}
