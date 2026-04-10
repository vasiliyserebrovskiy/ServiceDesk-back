package com.sitool.servicedesk.user.entity;

import com.sitool.servicedesk.role.entity.Role;
import com.sitool.servicedesk.utils.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

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
    private String avatar_url;

    public User(String lastname, String firstname, String email) {
        this.lastname = lastname;
        this.firstname = firstname;
        this.email = email;
    }

    @Override
    public String toString() {
        return "User{" +
                "firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", role=" + role +
                ", description='" + description + '\'' +
                ", avatar_url='" + avatar_url + '\'' +
                ", id=" + id +
                '}';
    }
}
