package com.sitool.servicedesk.user.entity;

import com.sitool.servicedesk.role.entity.Role;
import com.sitool.servicedesk.userprofile.entity.UserProfile;
import com.sitool.servicedesk.utils.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "users")
public class User extends BaseEntity {

    @Column(
            name = "email",
            unique = true,
            nullable = false
    )
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "is_active", nullable = false)
    private boolean isActive = true;

    @Column(name = "is_blocked", nullable = false)
    private boolean isBlocked = false;

    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    @OneToOne(
            mappedBy = "user",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private UserProfile profile;

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }


    @Override
    public String toString() {
        return "User {" +
                "email='" + email + '\'' +
                ", active=" + isActive +
                ", blocked=" + isBlocked +
                ", role=" + role +
                ", profile=" + profile +
                '}';
    }
}