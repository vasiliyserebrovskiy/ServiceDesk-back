package com.sitool.servicedesk.group.controller;

import com.sitool.servicedesk.exceptions.handling.response.ValidationErrorDto;
import com.sitool.servicedesk.group.dto.request.CreateGroupRequest;
import com.sitool.servicedesk.group.dto.request.UpdateGroupRequest;
import com.sitool.servicedesk.group.dto.response.GroupDto;
import com.sitool.servicedesk.user.dto.response.UserDto;
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
 * API contract for group-related operations.
 */
@Tag(name = "Groups", description = "Operations related to groups")
@RequestMapping("/api/v1/groups")
public interface GroupApi {

    @Operation(summary = "Register new group", description = "Creates a new group record.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Group created successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = GroupDto.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "id": "767ea865-8b32-454c-af05-52508be4033c",
                                      "name": "Support Team",
                                      "description":"Some description",
                                      "userIds":[
                                              "3453d552-f904-4f46-91c4-6b782553b421",
                                              "3453c371-f904-4f46-91c4-6a782553d592"
                                              ]
                                    }
                                    """))
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Group already exists"
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
    GroupDto createGroup(@Valid @RequestBody CreateGroupRequest createGroupRequest);

    @Operation(
            summary = "Update group",
            description = "Updates editable information for the specified group."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Group updated successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = GroupDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request data or UUID format"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Group not found"
            )
    })
    @PutMapping("/{groupId}")
    GroupDto updateGroup(@PathVariable UUID groupId, @Valid @RequestBody UpdateGroupRequest updateGroupRequest);

    @Operation(
            summary = "Delete group",
            description = "Deletes the group with the specified groupId (UUID)."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Group deleted successfully"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid UUID format"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Group not found"
            )
    })
    @DeleteMapping("/{groupId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteGroup(@PathVariable UUID groupId);

    @Operation(
            summary = "Get group by id",
            description = "Returns group information for the specified groupId (UUID)."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Group retrieved successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = GroupDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid UUID format"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Group not found"
            )
    })
    @GetMapping("/{groupId}")
    GroupDto getGroup(@PathVariable UUID groupId);

    @Operation(
            summary = "Get all groups",
            description = "Returns a list of all groups."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Groups retrieved successfully",
            content = @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = GroupDto.class))
            )
    )
    @GetMapping
    List<GroupDto> getAllGroups();
}
