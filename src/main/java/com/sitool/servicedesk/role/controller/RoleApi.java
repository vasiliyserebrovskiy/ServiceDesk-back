package com.sitool.servicedesk.role.controller;

import com.sitool.servicedesk.role.dto.response.RoleDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * API contract for role-related operations.
 */
@Tag(name = "Role controller", description = "Controller for Role operations")
@RequestMapping("/api/v1/roles")
public interface RoleApi {

    /**
     * Retrieves all available roles.
     *
     * @return list of available roles
     */
    @Operation(
            summary = "Get all roles",
            description = "Returns a list of all available system roles."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Roles successfully retrieved",
            content = @Content(
                    array = @ArraySchema(
                            schema = @Schema(implementation = RoleDto.class)
                    )
            )
    )
    @GetMapping
    List<RoleDto> getRoles();
}
