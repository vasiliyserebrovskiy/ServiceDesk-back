package com.sitool.servicedesk.usergroup.entity;

import com.sitool.servicedesk.group.entity.Group;
import com.sitool.servicedesk.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Join entity representing many-to-many relationship between {@link User} and {@link Group}.
 * <p>
 * Maps the {@code users_groups} table and uses a composite primary key
 * defined in {@link UserGroupId}.
 */
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "users_groups")
public class UserGroup {

    /**
     * Composite primary key (userId, groupId).
     */
    @EmbeddedId
    private UserGroupId id;

    /**
     * Associated {@link User} entity.
     */
    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;

    /**
     * Associated {@link Group} entity.
     */
    @ManyToOne
    @MapsId("groupId")
    @JoinColumn(name = "group_id")
    private Group group;


    @Override
    public String toString() {
        return "UserGroup{" +
                "id=" + id +
                ", userId=" + id.getUserId() +
                ", groupId=" + id.getGroupId() +
                '}';
    }
}
