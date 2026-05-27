package com.sitool.servicedesk.usergroup.repository;

import com.sitool.servicedesk.usergroup.entity.UserGroup;
import com.sitool.servicedesk.usergroup.entity.UserGroupId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Repository for managing {@link UserGroup} relationships.
 * Provides access to user-group mapping operations.
 */
public interface UserGroupRepository extends JpaRepository<UserGroup, UserGroupId> {
    /**
     * Retrieves all user IDs assigned to a specific group.
     *
     * @param groupId group identifier
     * @return list of user IDs in the group
     */
    @Query("""
                select ug.user.id
                from UserGroup ug
                where ug.group.id = :groupId
            """)
    List<UUID> findUserIdsByGroupId(UUID groupId);

    /**
     * Removes selected users from a group.
     *
     * @param groupId group identifier
     * @param userIds set of user IDs to remove
     */
    @Modifying
    @Query("""
                DELETE FROM UserGroup ug
                WHERE ug.group.id = :groupId
                AND ug.user.id IN :userIds
            """)
    void deleteUsersFromGroup(UUID groupId, Set<UUID> userIds);
}
