package com.sitool.servicedesk.group.service;

import com.sitool.servicedesk.group.dto.request.CreateGroupRequest;
import com.sitool.servicedesk.group.dto.request.UpdateGroupRequest;
import com.sitool.servicedesk.group.dto.response.GroupDto;
import jakarta.validation.Valid;

import java.util.List;
import java.util.UUID;

public interface GroupService {
    GroupDto createGroup(CreateGroupRequest createGroupRequest);

    GroupDto updateGroup(UUID groupId, UpdateGroupRequest updateGroupRequest);

    void deleteGroup(UUID groupId);

    GroupDto getGroup(UUID groupId);

    List<GroupDto> getAllGroups();
}
