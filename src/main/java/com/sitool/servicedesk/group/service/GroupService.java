package com.sitool.servicedesk.group.service;

import com.sitool.servicedesk.group.dto.request.CreateGroupRequest;
import com.sitool.servicedesk.group.dto.response.GroupDto;

public interface GroupService {
    GroupDto createGroup(CreateGroupRequest createGroupRequest);
}
