package com.sitool.servicedesk.usergroup.repository;

import com.sitool.servicedesk.usergroup.entity.UserGroup;
import com.sitool.servicedesk.usergroup.entity.UserGroupId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface UserGroupRepository extends JpaRepository<UserGroup, UserGroupId> {
    @Query("""
                select ug.user.id
                from UserGroup ug
                where ug.group.id = :groupId
            """)
    List<UUID> findUserIdsByGroupId(UUID groupId);

    @Modifying
    @Query("""
                DELETE FROM UserGroup ug
                WHERE ug.group.id = :groupId
                AND ug.user.id IN :userIds
            """)
    void deleteUsersFromGroup(UUID groupId, Set<UUID> userIds);
}
