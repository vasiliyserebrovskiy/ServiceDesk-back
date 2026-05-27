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
import com.sitool.servicedesk.usergroup.repository.UserGroupRepository;
import com.sitool.servicedesk.utils.BaseEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GroupServiceImplTest {

    @Mock
    private GroupRepository groupRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserGroupRepository userGroupRepository;

    @Mock
    private GroupMapper groupMapper;

    @InjectMocks
    private GroupServiceImpl groupService;

    private UUID groupId;
    private UUID userId;

    @BeforeEach
    void setUp() {
        groupId = UUID.randomUUID();
        userId = UUID.randomUUID();
    }

    @Test
    @DisplayName("Should create group successfully")
    void createGroup_shouldCreateGroup() {

        CreateGroupRequest request = new CreateGroupRequest(
                "Support Team",
                "Some description",
                List.of(userId)
        );

        User user = new User();
        setId(user, userId);

        GroupDto expectedDto = new GroupDto(
                groupId,
                "Support Team",
                "Some description",
                List.of(userId)
        );

        when(groupRepository.existsByNameIgnoreCase("Support Team"))
                .thenReturn(false);

        when(userRepository.findAllById(any()))
                .thenReturn(List.of(user));

        when(groupMapper.groupToGroupDto(any(Group.class), anyList()))
                .thenReturn(expectedDto);

        GroupDto result = groupService.createGroup(request);

        assertNotNull(result);
        assertEquals("Support Team", result.name());

        verify(groupRepository).save(any(Group.class));
        verify(userGroupRepository).saveAll(anyList());
        verify(groupMapper).groupToGroupDto(any(Group.class), anyList());
    }

    @Test
    @DisplayName("Should throw exception when group already exists")
    void createGroup_shouldThrowException_whenGroupExists() {

        CreateGroupRequest request = new CreateGroupRequest(
                "Support Team",
                "Description",
                List.of()
        );

        when(groupRepository.existsByNameIgnoreCase("Support Team"))
                .thenReturn(true);

        assertThrows(
                GroupAlreadyExistException.class,
                () -> groupService.createGroup(request)
        );

        verify(groupRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should update group successfully")
    void updateGroup_shouldUpdateGroup() {

        UUID secondUserId = UUID.randomUUID();

        UpdateGroupRequest request = new UpdateGroupRequest(
                "Updated Team",
                "Updated description",
                List.of(secondUserId)
        );

        Group group = new Group();
        setId(group, groupId);
        group.setName("Old Team");
        group.setDescription("Old description");

        User user = new User();
        setId(user, userId);

        GroupDto expectedDto = new GroupDto(
                groupId,
                "Updated Team",
                "Updated description",
                List.of(secondUserId)
        );

        when(groupRepository.findById(groupId))
                .thenReturn(Optional.of(group));

        when(groupRepository.existsByNameIgnoreCase("Updated Team"))
                .thenReturn(false);

        when(userGroupRepository.findUserIdsByGroupId(groupId))
                .thenReturn(List.of(userId));

        when(userRepository.findAllById(anySet()))
                .thenReturn(List.of(user));

        when(groupMapper.groupToGroupDto(any(Group.class), anyList()))
                .thenReturn(expectedDto);

        GroupDto result = groupService.updateGroup(groupId, request);

        assertNotNull(result);
        assertEquals("Updated Team", result.name());

        verify(userGroupRepository)
                .deleteUsersFromGroup(eq(groupId), anySet());

        verify(userGroupRepository)
                .saveAll(anyList());
    }

    @Test
    @DisplayName("Should throw exception when updating missing group")
    void updateGroup_shouldThrowException_whenGroupNotFound() {

        UpdateGroupRequest request = new UpdateGroupRequest(
                "Updated Team",
                "Description",
                List.of()
        );

        when(groupRepository.findById(groupId))
                .thenReturn(Optional.empty());

        assertThrows(
                GroupNotFoundException.class,
                () -> groupService.updateGroup(groupId, request)
        );
    }

    @Test
    @DisplayName("Should delete group successfully")
    void deleteGroup_shouldDeleteGroup() {

        Group group = new Group();
        setId(group,groupId);

        when(groupRepository.findById(groupId))
                .thenReturn(Optional.of(group));

        groupService.deleteGroup(groupId);

        verify(groupRepository).delete(group);
    }

    @Test
    @DisplayName("Should throw exception when deleting missing group")
    void deleteGroup_shouldThrowException_whenGroupNotFound() {

        when(groupRepository.findById(groupId))
                .thenReturn(Optional.empty());

        assertThrows(
                GroupNotFoundException.class,
                () -> groupService.deleteGroup(groupId)
        );
    }

    @Test
    @DisplayName("Should return group by id")
    void getGroup_shouldReturnGroup() {

        Group group = new Group();
        setId(group, groupId);
        group.setName("Support Team");

        GroupDto expectedDto = new GroupDto(
                groupId,
                "Support Team",
                "Description",
                List.of(userId)
        );

        when(groupRepository.findById(groupId))
                .thenReturn(Optional.of(group));

        when(userGroupRepository.findUserIdsByGroupId(groupId))
                .thenReturn(List.of(userId));

        when(groupMapper.groupToGroupDto(group, List.of(userId)))
                .thenReturn(expectedDto);

        GroupDto result = groupService.getGroup(groupId);

        assertNotNull(result);
        assertEquals(groupId, result.id());

        verify(groupMapper)
                .groupToGroupDto(group, List.of(userId));
    }

    @Test
    @DisplayName("Should return all groups")
    void getAllGroups_shouldReturnAllGroups() {

        Group first = new Group();
        setId(first, UUID.randomUUID());
        first.setName("First");

        Group second = new Group();
        setId(second, UUID.randomUUID());
        second.setName("Second");

        when(groupRepository.findAll())
                .thenReturn(List.of(first, second));

        when(userGroupRepository.findUserIdsByGroupId(any()))
                .thenReturn(List.of());

        when(groupMapper.groupToGroupDto(any(Group.class), anyList()))
                .thenAnswer(invocation -> {
                    Group g = invocation.getArgument(0);

                    return new GroupDto(
                            g.getId(),
                            g.getName(),
                            g.getDescription(),
                            List.of()
                    );
                });

        List<GroupDto> result = groupService.getAllGroups();

        assertEquals(2, result.size());

        verify(groupRepository).findAll();
        verify(groupMapper, times(2))
                .groupToGroupDto(any(Group.class), anyList());
    }

    private void setId(Object entity, UUID id) {
        try {
            Field field = BaseEntity.class.getDeclaredField("id");
            field.setAccessible(true);
            field.set(entity, id);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}