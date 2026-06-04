package com.sitool.servicedesk.group.controller;

import com.sitool.servicedesk.group.dto.request.CreateGroupRequest;
import com.sitool.servicedesk.group.dto.request.UpdateGroupRequest;
import com.sitool.servicedesk.group.dto.response.GroupDto;
import com.sitool.servicedesk.group.service.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

/**
 * REST controller that delegates group operations to the GroupService.
 */
@RestController
@RequiredArgsConstructor
public class GroupController implements GroupApi {

    private final GroupService groupService;

    @Override
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public GroupDto createGroup(CreateGroupRequest request) {
        return groupService.createGroup(request);
    }

    @Override
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public GroupDto updateGroup(UUID groupId, UpdateGroupRequest request) {
        return groupService.updateGroup(groupId, request);
    }

    @Override
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public void deleteGroup(UUID groupId) {
        groupService.deleteGroup(groupId);
    }

    @Override
    public GroupDto getGroup(UUID groupId) {
        return groupService.getGroup(groupId);
    }

    @Override
    public List<GroupDto> getAllGroups() {
        return groupService.getAllGroups();
    }
}
