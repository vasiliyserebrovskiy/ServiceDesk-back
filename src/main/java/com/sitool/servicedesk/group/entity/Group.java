package com.sitool.servicedesk.group.entity;

import com.sitool.servicedesk.usergroup.entity.UserGroup;
import com.sitool.servicedesk.utils.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * Entity representing a user group.
 *
 * Groups are used to organize users by roles, permissions,
 * or business-specific logic.
 *
 * Extends BaseEntity which provides id and audit fields.
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "groups")
public class Group extends BaseEntity {

    /**
     * Unique group name.
     * <p>
     * This field cannot be null and must be unique.
     */
    @Column(name="name", nullable = false, unique = true, length = 200)
    private String name;

    /**
     * Optional group description.
     */
    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy = "group")
    private List<UserGroup> userGroups;

    /**
     * Returns string representation of the group entity.
     *
     * @return string representation of Group
     */
    @Override
    public String toString() {
        return "Group{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
