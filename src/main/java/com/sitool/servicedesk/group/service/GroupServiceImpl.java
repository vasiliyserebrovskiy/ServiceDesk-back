package com.sitool.servicedesk.group.service;

import com.sitool.servicedesk.group.dto.request.CreateGroupRequest;
import com.sitool.servicedesk.group.dto.response.GroupDto;
import com.sitool.servicedesk.group.entity.Group;
import com.sitool.servicedesk.group.exceptions.GroupAlreadyExistException;
import com.sitool.servicedesk.group.mapper.GroupMapper;
import com.sitool.servicedesk.group.repository.GroupRepository;
import com.sitool.servicedesk.user.entity.User;
import com.sitool.servicedesk.user.repository.UserRepository;
import com.sitool.servicedesk.usergroup.entity.UserGroup;
import com.sitool.servicedesk.usergroup.entity.UserGroupId;
import com.sitool.servicedesk.usergroup.repository.UserGroupRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class GroupServiceImpl implements GroupService {

    private final GroupRepository groupRepository;
    private final UserRepository userRepository;
    private final UserGroupRepository userGroupRepository;
    private final GroupMapper groupMapper;

    @Override
    @Transactional
    public GroupDto createGroup(CreateGroupRequest createGroupRequest) {
        if(groupRepository.existsByNameIgnoreCase(createGroupRequest.name())) {
            log.info("User with email {} already exists", createGroupRequest.name());
            throw new GroupAlreadyExistException();
        }

        // Create new group
        Group newGroup = new Group();
        newGroup.setName(createGroupRequest.name());
        newGroup.setDescription(createGroupRequest.description());

        groupRepository.save(newGroup);

        List<User> users = userRepository.findAllById(createGroupRequest.userIds());

        // Save UserGroups records
        List<UserGroup> links = new ArrayList<>();

        for (User user : users) {
            UserGroup ug = new UserGroup();
            ug.setUser(user);
            ug.setGroup(newGroup);
            ug.setId(new UserGroupId(user.getId(),newGroup.getId()));
            links.add(ug);
        }

        userGroupRepository.saveAll(links);

        List<UUID> userIds = users.stream()
                .map(User::getId)
                .toList();

        return groupMapper.groupToGroupDto(newGroup, userIds);
    }
}
