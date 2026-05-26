package com.sitool.servicedesk.group.controller;

import com.sitool.servicedesk.exceptions.handling.response.ValidationErrorDto;
import com.sitool.servicedesk.group.dto.request.CreateGroupRequest;
import com.sitool.servicedesk.group.dto.request.UpdateGroupRequest;
import com.sitool.servicedesk.group.dto.response.GroupDto;
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
 * Api contract for grope-related operations.
 */
@Tag(name = "Group controller", description = "Controller for Group operations")
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
                                      "name": "Vasiliy",
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
                                      { "field": "name": "must be a well-formed name " },
                                      { "field": "name": "must not be blank" }
                                    ]
                                    """))
            )
    })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/create")
    GroupDto createGroup(@Valid @RequestBody CreateGroupRequest createGroupRequest);

    @PatchMapping("/{groupId}")
    GroupDto updateGroup(@Valid @PathVariable UUID groupId, @RequestBody UpdateGroupRequest updateGroupRequest);

    @DeleteMapping("/{groupId}")
    void deleteGroup(@PathVariable UUID groupId);

    @GetMapping("/{groupId}")
    GroupDto getGroup(@PathVariable UUID groupId);

    @GetMapping
    List<GroupDto> getAllGroups();
}
