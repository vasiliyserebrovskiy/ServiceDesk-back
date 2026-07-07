package com.sitool.servicedesk.servicenow.settings.entity;


import com.sitool.servicedesk.servicenow.settings.converter.EncryptedStringConverter;
import com.sitool.servicedesk.utils.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * Entity representing ServiceNow integration settings.
 * <p>
 * Stores the endpoint, username and password required to authenticate
 * against the ServiceNow Scripted REST API when creating incidents.
 * <p>
 * The application is designed to keep at most one row in the
 * {@code servicenow_settings} table at any given time. This is
 * enforced by the service layer (create-or-update logic), not by
 * a database constraint or a code-level singleton guarantee.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "servicenow_settings")
public class ServiceNowSettings extends BaseEntity {

    @Column(name = "endpoint", nullable = false)
    private String endpoint;

    @Column(name = "username", nullable = false)
    private String username;

    @Convert(converter = EncryptedStringConverter.class)
    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    @UpdateTimestamp
    private LocalDateTime updatedAt;


    @Override
    public String toString() {
        return "ServiceNowSettings {" +
                "id =" + id +
                ", endpoint='" + endpoint + '\'' +
                ", username='" + username + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
