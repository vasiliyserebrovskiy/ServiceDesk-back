package com.sitool.servicedesk.usergroup.entity;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * Composite identifier for the UserGroup entity.
 * <p>
 * This class represents a composite primary key consisting of:
 * <ul>
 *     <li>userId — identifier of the user</li>
 *     <li>groupId — identifier of the group</li>
 * </ul>
 * <p>
 * The class is marked with {@code @Embeddable} so it can be embedded
 * into an entity as a composite key using {@code @EmbeddedId}.
 */

@Embeddable
public class UserGroupId implements Serializable {

    /**
     * Identifier of the user.
     */
    private UUID userId;
    /**
     * Identifier of the group.
     */
    private UUID groupId;

    protected UserGroupId() {
    }

    public UserGroupId(UUID userId, UUID groupId) {
        this.userId = userId;
        this.groupId = groupId;
    }

    public UUID getUserId() {
        return userId;
    }

    public UUID getGroupId() {
        return groupId;
    }

    /**
     * Compares this identifier with another object.
     *
     * @param o object to compare
     * @return {@code true} if both identifiers contain the same userId and groupId,
     * otherwise {@code false}
     */
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        UserGroupId that = (UserGroupId) o;
        return Objects.equals(userId, that.userId) && Objects.equals(groupId, that.groupId);
    }

    /**
     * Generates hash code based on userId and groupId.
     *
     * @return generated hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(userId, groupId);
    }
}
