package com.sitool.servicedesk.role.entity;

import com.sitool.servicedesk.utils.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "roles")
public class Role extends BaseEntity {

    @Column(name="name", nullable = false, unique = true)
    private String name;

    @Column(name="description")
    private String description;

    @Column(name="default_role", nullable = false)
    private boolean defaultRole=false;


    @Override
    public String toString() {
        return "Role{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", defaultRole=" + defaultRole +
                ", id=" + id +
                '}';
    }
}
