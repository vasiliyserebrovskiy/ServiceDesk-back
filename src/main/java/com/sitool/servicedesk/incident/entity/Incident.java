package com.sitool.servicedesk.incident.entity;

import com.sitool.servicedesk.category.entity.Category;
import com.sitool.servicedesk.ci.entity.CI;
import com.sitool.servicedesk.group.entity.Group;
import com.sitool.servicedesk.shared.enums.Impact;
import com.sitool.servicedesk.shared.enums.Priority;
import com.sitool.servicedesk.shared.enums.Urgency;
import com.sitool.servicedesk.status.entity.Status;
import com.sitool.servicedesk.sybcategory.entity.Subcategory;
import com.sitool.servicedesk.user.entity.User;
import com.sitool.servicedesk.utils.BaseEntity;
import jakarta.persistence.*;
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
@Table(name = "incidents")
public class Incident extends BaseEntity {

    @Column(name = "number", nullable = false)
    private String number;

    @ManyToOne
    @JoinColumn(name = "requester_id", nullable = false)
    private User requester;

    @ManyToOne
    @JoinColumn(name = "category_id",  nullable = false)
    private Category category;

    @ManyToOne
    @JoinColumn(name = "subcategory_id")
    private Subcategory subcategory;

    @ManyToOne
    @JoinColumn(name = "status_id", nullable = false)
    private Status status;

    @Enumerated(EnumType.STRING)
    @Column(name = "priority", nullable = false)
    private Priority priority;

    @Enumerated(EnumType.STRING)
    @Column(name = "impact", nullable = false)
    private Impact impact;

    @Enumerated(EnumType.STRING)
    @Column(name = "urgency", nullable = false)
    private Urgency urgency;

    @ManyToOne
    @JoinColumn(name = "configuration_item_id")
    private CI ci;

    @ManyToOne
    @JoinColumn(name = "group_id")
    private Group group;

    @ManyToOne
    @JoinColumn(name = "assignee_id")
    private User assignee;

    @Column(name = "short_description", nullable = false)
    private String shortDescription;

    @Column(name = "description")
    private String description;

    @Column(name = "servicenow_number")
    private String servicenowNumber;

    @Column(name = "servicenow_synced")
    private Boolean servicenowSynced = false;

    @Column(name = "servicenow_synced_at")
    private LocalDateTime servicenowSyncedAt;

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    @UpdateTimestamp
    private LocalDateTime updatedAt;


    @Override
    public String toString() {
        return "Incident{" +
                "id=" + id +
                ", number='" + number + '\'' +
                ", status=" + status +
                ", priority=" + priority +
                ", impact=" + impact +
                ", urgency=" + urgency +
                ", shortDescription='" + shortDescription + '\'' +
                ", description='" + description + '\'' +
                ", servicenowNumber='" + servicenowNumber + '\'' +
                ", servicenowSynced=" + servicenowSynced +
                ", servicenowSyncedAt=" + servicenowSyncedAt +
                '}';
    }
}
