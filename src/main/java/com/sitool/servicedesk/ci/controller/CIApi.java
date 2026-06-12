package com.sitool.servicedesk.ci.controller;

import com.sitool.servicedesk.category.dto.response.CategoryDto;
import com.sitool.servicedesk.ci.dto.request.CreateCIRequest;
import com.sitool.servicedesk.ci.dto.request.UpdateCIRequest;
import com.sitool.servicedesk.ci.dto.response.CIDto;
import com.sitool.servicedesk.exceptions.handling.response.ValidationErrorDto;
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
 * API contract for configuration item-related operations.
 */
@Tag(name = "CI", description = "Operations related to configuration item")
@RequestMapping("/api/v1/cis")
public interface CIApi {
    @Operation(summary = "Register new configuration item", description = "Creates a new configuration item record.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Configuration item created successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CIDto.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "id": "767ea865-8b32-454c-af05-52508be4033c",
                                      "name": "Core-SW-01",
                                      "description": "Core network switch located in server room A",
                                      "type": "Network Equipment",
                                      "manufacturer": "Cisco",
                                      "serialNumber": "FCW2142L0QK",
                                      "model": "Catalyst 9300"
                                    }
                                    """))
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Configuration item already exists"
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
    CIDto createCI(@Valid @RequestBody CreateCIRequest request);

    @Operation(
            summary = "Update configuration item",
            description = "Updates editable information for the specified configuration item."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Configuration item updated successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CIDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request data or UUID format"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Configuration item not found"
            )
    })
    @PutMapping("/{ciId}")
    CIDto updateCI(@PathVariable UUID ciId, @Valid @RequestBody UpdateCIRequest request);

    @Operation(
            summary = "Delete configuration item",
            description = "Deletes the configuration item with the specified ciId (UUID)."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Configuration item deleted successfully"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid UUID format"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Configuration item not found"
            )
    })
    @DeleteMapping("/{ciId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteCI(@PathVariable UUID ciId);

    @Operation(
            summary = "Get configuration item by id",
            description = "Returns configuration item information for the specified ciId (UUID)."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Configuration item retrieved successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CIDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid UUID format"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Configuration item not found"
            )
    })
    @GetMapping("/{ciId}")
    CIDto getCIById(@PathVariable UUID ciId);

    @Operation(
            summary = "Get all configuration items",
            description = "Returns a list of all configuration items."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Configuration items retrieved successfully",
            content = @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = CIDto.class))
            )
    )
    @GetMapping
    List<CIDto> getAllCI();
}
