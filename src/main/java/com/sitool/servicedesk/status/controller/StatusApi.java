package com.sitool.servicedesk.status.controller;

import com.sitool.servicedesk.exceptions.handling.response.ValidationErrorDto;
import com.sitool.servicedesk.status.dto.request.CreateStatusRequest;
import com.sitool.servicedesk.status.dto.request.UpdateStatusRequest;
import com.sitool.servicedesk.status.dto.response.StatusDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * API contract for status-related operations.
 */
@Tag(name = "Statuses", description = "Operations related to statuses")
@RequestMapping("/api/v1/statuses")
public interface StatusApi {
    @Operation(summary = "Register new status", description = "Creates a new status record.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Status created successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = StatusDto.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "id": "767ea865-8b32-454c-af05-52508be4033c",
                                      "name": "Open",
                                      "description":"Open status for incidents",
                                      "isIncident":true,
                                      "isProblem":false,
                                      "isRequest":false,
                                      "isChange":false,
                                      "isTask": false
                                    }
                                    """))
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Status already exists"
            ),
            @ApiResponse(responseCode = "400", description = "Invalid request payload",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = ValidationErrorDto.class)),
                            examples = @ExampleObject(value = """
                                    [
                                      { "field": "name",
                                        "message": "must be a well-formed name" },
                                      { "field": "name",
                                        "message": "must not be blank" }
                                    ]
                                    """))
            )
    })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    StatusDto createStatus(@Valid @RequestBody CreateStatusRequest request);

    @Operation(
            summary = "Update status",
            description = "Updates editable information for the specified status."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Status updated successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = StatusDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request data or UUID format"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Status not found"
            )
    })
    @PutMapping("/{statusId}")
    StatusDto updateStatus(@PathVariable UUID statusId, @Valid @RequestBody UpdateStatusRequest request);

    @Operation(
            summary = "Delete status",
            description = "Deletes the status with the specified statusId (UUID)."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Status deleted successfully"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid UUID format"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Status not found"
            )
    })
    @DeleteMapping("/{statusId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteStatus(@PathVariable UUID statusId);

    @Operation(
            summary = "Get status by id",
            description = "Returns status information for the specified statusId (UUID)."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Status retrieved successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = StatusDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid UUID format"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Status not found"
            )
    })
    @GetMapping("/{statusId}")
    StatusDto getStatus(@PathVariable UUID statusId);

    @Operation(
            summary = "Get all statuses",
            description = "Returns a list of all statuses."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Statuses retrieved successfully",
            content = @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = StatusDto.class))
            )
    )
    @GetMapping
    List<StatusDto> getAllStatuses(@RequestParam(required = false) String type);


}
