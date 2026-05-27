package com.sitool.servicedesk.group.service;

import com.sitool.servicedesk.group.dto.request.CreateGroupRequest;
import com.sitool.servicedesk.group.dto.request.UpdateGroupRequest;
import com.sitool.servicedesk.group.dto.response.GroupDto;
import jakarta.validation.Valid;

import java.util.List;
import java.util.UUID;

/**
 * Service layer for managing groups.
 * <p>
 * Provides business operations for creating, updating, retrieving,
 * and deleting groups.
 */
public interface GroupService {
    /**
     * Creates a new group.
     *
     * @param request data required to create a group
     * @return created group as DTO
     */
    GroupDto createGroup(CreateGroupRequest request);

    /**
     * Updates an existing group by its ID.
     *
     * @param groupId identifier of the group to update
     * @param request updated group data
     * @return updated group as DTO
     */
    GroupDto updateGroup(UUID groupId, UpdateGroupRequest request);

    /**
     * Deletes a group by its ID.
     *
     * @param groupId identifier of the group to delete
     */
    void deleteGroup(UUID groupId);

    /**
     * Retrieves a group by its ID.
     *
     * @param groupId identifier of the group
     * @return group as DTO
     */
    GroupDto getGroup(UUID groupId);

    /**
     * Retrieves all groups.
     *
     * @return list of all groups as DTOs
     */
    List<GroupDto> getAllGroups();
}
