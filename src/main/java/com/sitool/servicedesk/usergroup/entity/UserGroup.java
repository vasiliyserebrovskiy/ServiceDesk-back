package com.sitool.servicedesk.usergroup.entity;

import com.sitool.servicedesk.group.entity.Group;
import com.sitool.servicedesk.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entity representing the relationship between users and groups.
 * <p>
 * This entity maps the {@code users_groups} join table and stores
 * associations between {@link User} and {@link Group}.
 * <p>
 * A composite primary key is used through {@link UserGroupId}.
 */
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "users_groups")
public class UserGroup {

    /**
     * Composite primary key containing userId and groupId.
     */
    @EmbeddedId
    private UserGroupId id;

    /**
     * Associated user entity.
     * <p>
     * The {@code userId} value from {@link UserGroupId}
     * is mapped to this relationship using {@code @MapsId}.
     */
    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;

    /**
     * Associated group entity.
     * <p>
     * The {@code groupId} value from {@link UserGroupId}
     * is mapped to this relationship using {@code @MapsId}.
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
