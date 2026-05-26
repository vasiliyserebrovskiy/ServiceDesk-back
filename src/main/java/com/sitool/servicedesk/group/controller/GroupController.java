package com.sitool.servicedesk.group.controller;

import com.sitool.servicedesk.group.dto.request.CreateGroupRequest;
import com.sitool.servicedesk.group.dto.request.UpdateGroupRequest;
import com.sitool.servicedesk.group.dto.response.GroupDto;
import com.sitool.servicedesk.group.service.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class GroupController implements GroupApi {

    private final GroupService groupService;

    @Override
    public GroupDto createGroup(CreateGroupRequest createGroupRequest) {
        return groupService.createGroup(createGroupRequest);
    }

    @Override
    public GroupDto updateGroup(UUID groupId, UpdateGroupRequest updateGroupRequest) {
        return groupService.updateGroup(groupId, updateGroupRequest);
    }

    @Override
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
