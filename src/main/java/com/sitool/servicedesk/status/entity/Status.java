package com.sitool.servicedesk.status.entity;

import com.sitool.servicedesk.utils.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "statuses")
public class Status extends BaseEntity {
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "is_incident", nullable = false)
    private Boolean isIncident;

    @Column(name = "is_problem", nullable = false)
    private Boolean isProblem;

    @Column(name = "is_request", nullable = false)
    private Boolean isRequest;

    @Column(name = "is_change", nullable = false)
    private Boolean isChange;

    @Column(name = "is_task", nullable = false)
    private Boolean isTask;

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Override
    public String toString() {
        return "Status{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", isIncident=" + isIncident +
                ", isProblem=" + isProblem +
                ", isRequest=" + isRequest +
                ", isChange=" + isChange +
                ", isTask=" + isTask +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
