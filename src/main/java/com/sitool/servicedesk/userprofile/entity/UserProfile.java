package com.sitool.servicedesk.userprofile.entity;

import com.sitool.servicedesk.user.entity.User;
import com.sitool.servicedesk.utils.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * JPA entity representing a user's profile information.
 *
 * <p>Stores personal and presentation-related data associated with a system user,
 * such as first name, last name, profile description, and avatar URL.</p>
 *
 * <p>This entity is separated from the {@link User} entity to isolate
 * authentication and authorization logic from user profile data.</p>
 *
 * <p>Each profile is linked to exactly one {@link User} through a one-to-one
 * relationship.</p>
 *
 * <p>Extends {@link BaseEntity} to inherit common fields such as identifier
 * and timestamps.</p>
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "user_profiles")
public class UserProfile extends BaseEntity {

    @Column(name = "firstname", nullable = false)
    private String firstname;

    @Column(name = "lastname", nullable = false)
    private String lastname;

    @Column(name = "description")
    private String description;

    @Column(name = "avatar_url")
    private String avatarUrl;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    public UserProfile(String firstname, String lastname) {
        this.firstname = firstname;
        this.lastname = lastname;
    }

    @Override
    public String toString() {
        return "UserProfile {" +
                " firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", description='" + description + '\'' +
                ", avatarUrl='" + avatarUrl + '\'' +
                '}';
    }
}