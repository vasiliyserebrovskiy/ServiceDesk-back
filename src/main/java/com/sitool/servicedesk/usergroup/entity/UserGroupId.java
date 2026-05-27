package com.sitool.servicedesk.usergroup.entity;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * Composite primary key for {@link com.sitool.servicedesk.usergroup.entity.UserGroup}.
 * <p>
 * Represents a combination of userId and groupId used as an embedded identifier.
 */
@Embeddable
public class UserGroupId implements Serializable {

    /** User identifier. */
    private UUID userId;

    /** Group identifier. */
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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        UserGroupId that = (UserGroupId) o;
        return Objects.equals(userId, that.userId) && Objects.equals(groupId, that.groupId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, groupId);
    }
}
