package com.sitool.servicedesk.user.entity;

import com.sitool.servicedesk.role.entity.Role;
import com.sitool.servicedesk.utils.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * JPA entity representing a system user.
 *
 * <p>Stores authentication and profile-related information for application users.
 * This entity is persisted in the "users" table and linked to a {@link Role} entity
 * to define user permissions within the system.</p>
 *
 * <p>Extends {@link BaseEntity} to inherit common fields such as identifier and timestamps.</p>
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "users")
public class User extends BaseEntity {

    @Column(name="firstname", nullable = false)
    private String firstname;

    @Column(name="lastname", nullable = false)
    private String lastname;

    @Column(
            name="email",
            unique = true,
            nullable = false
    )
    private String email;

    @Column(name="password", nullable = false)
    private String password;

    @ManyToOne
    @JoinColumn(name="role_id", nullable = false)
    private Role role;

    @Column(name="description")
    private String description;

    @Column(name="avatar_url")
    private String avatarUrl;

    public User(String firstname, String lastname, String email) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
    }

    @Override
    public String toString() {
        return "User{" +
                "firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", email='" + email + '\'' +
                ", role=" + role +
                ", description='" + description + '\'' +
                ", avatar_url='" + avatarUrl + '\'' +
                ", id=" + id +
                '}';
    }
}
