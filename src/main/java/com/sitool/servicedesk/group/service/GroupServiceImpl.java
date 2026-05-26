package com.sitool.servicedesk.group.service;

import com.sitool.servicedesk.group.dto.request.CreateGroupRequest;
import com.sitool.servicedesk.group.dto.request.UpdateGroupRequest;
import com.sitool.servicedesk.group.dto.response.GroupDto;
import com.sitool.servicedesk.group.entity.Group;
import com.sitool.servicedesk.group.exceptions.GroupAlreadyExistException;
import com.sitool.servicedesk.group.exceptions.GroupNotFoundException;
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

import java.util.*;

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
        String normalizedName = createGroupRequest.name().trim();
        if(groupRepository.existsByNameIgnoreCase(normalizedName)) {
            log.info("createGroup: Group with name {} already exists", normalizedName);
            throw new GroupAlreadyExistException();
        }

        // Create new group
        Group newGroup = new Group();
        newGroup.setName(normalizedName);
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

    @Override
    @Transactional
    public GroupDto updateGroup(UUID groupId, UpdateGroupRequest updateGroupRequest) {
        String normalizedName = updateGroupRequest.name().trim();

        Group currentGroup = groupRepository.findById(groupId).orElseThrow(() -> {
            log.error("updateGroup: Group with id {} not found", groupId);
            return new GroupNotFoundException();
        });

        if(!currentGroup.getName().equalsIgnoreCase(normalizedName) && groupRepository.existsByNameIgnoreCase(normalizedName)) {
            log.info("updateGroup: Group with name {} already exists", normalizedName);
            throw new GroupAlreadyExistException();
        }

        if (!currentGroup.getName().equals(normalizedName)) {
            currentGroup.setName(normalizedName);
        }

        if (!Objects.equals(currentGroup.getDescription(), updateGroupRequest.description())) {
            currentGroup.setDescription(updateGroupRequest.description());
        }

        Set<UUID> currentGroupUsers = new HashSet<>(userGroupRepository.findUserIdsByGroupId(currentGroup.getId()));
        Set<UUID> newGroupUsers = new HashSet<>(updateGroupRequest.userIds());

        // Users id we need to delete
        Set<UUID> usersToDelete = new HashSet<>(currentGroupUsers);
        usersToDelete.removeAll(newGroupUsers);

        // Users id we need to add
        Set<UUID> usersToAdd = new HashSet<>(newGroupUsers);
        usersToAdd.removeAll(currentGroupUsers);

        // Remove old Users
        if(!usersToDelete.isEmpty()) {
            userGroupRepository.deleteUsersFromGroup(currentGroup.getId(), usersToDelete);
        }

        // Add new users
        if (!usersToAdd.isEmpty()) {
            List<User> users = userRepository.findAllById(usersToAdd);

            List<UserGroup> newRelations = users.stream()
                    .map(user -> {
                        UserGroup ug = new UserGroup();
                        ug.setId(new UserGroupId(user.getId(), currentGroup.getId()));
                        ug.setUser(user);
                        ug.setGroup(currentGroup);

                        return ug;
                    })
                    .toList();

            userGroupRepository.saveAll(newRelations);
        }
        return groupMapper.groupToGroupDto(currentGroup, new ArrayList<>(newGroupUsers));
    }

    @Override
    public void deleteGroup(UUID groupId) {
        Group currentGroup = groupRepository.findById(groupId).orElseThrow(() -> {
            log.error("deleteGroup: Group with id {} not found", groupId);
            return new GroupNotFoundException();
        });
        groupRepository.delete(currentGroup);
    }

    @Override
    public GroupDto getGroup(UUID groupId) {
        Group currentGroup = groupRepository.findById(groupId).orElseThrow(() -> {
            log.error("getGroup: Group with id {} not found", groupId);
            return new GroupNotFoundException();
        });

        List<UUID> userIds =  userGroupRepository.findUserIdsByGroupId(currentGroup.getId());

        return groupMapper.groupToGroupDto(currentGroup, userIds);
    }

    public List<GroupDto> getAllGroups() {

        List<Group> groups = groupRepository.findAll();

        return groups.stream()
                .map(group -> {
                    List<UUID> userIds =
                            userGroupRepository.findUserIdsByGroupId(group.getId());

                    return groupMapper.groupToGroupDto(group, userIds);
                })
                .toList();
    }
}
