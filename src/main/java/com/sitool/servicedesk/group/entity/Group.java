package com.sitool.servicedesk.group.entity;

import com.sitool.servicedesk.utils.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entity representing a user group.
 * <p>
 * A group can be used to объединять users by roles,
 * permissions, responsibilities, or any business-related logic.
 * <p>
 * This entity extends {@link BaseEntity}, which provides
 * common fields such as identifier and audit information.
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
    @NotNull
    @Column(name="name", nullable = false, unique = true, length = 200)
    private String name;

    /**
     * Optional group description.
     */
    @Column(name = "description")
    private String description;

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
