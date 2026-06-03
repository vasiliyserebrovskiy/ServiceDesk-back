package com.sitool.servicedesk.category.entity;

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

/**
 * Represents a service desk category.
 *
 * <p>Categories are used to classify tickets by their type and business purpose.
 * A category can be assigned to one or more ticket types:
 * Incident, Problem, Request, or Change.</p>
 *
 * <p>Examples:
 * <ul>
 *     <li>Hardware Issue (Incident)</li>
 *     <li>Software Installation (Request)</li>
 *     <li>Root Cause Analysis (Problem)</li>
 *     <li>Server Upgrade (Change)</li>
 * </ul>
 * </p>
 *
 * <p>The entity is mapped to the {@code categories} database table.</p>
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "categories")
public class Category extends BaseEntity {
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "is_incident", nullable = false)
    private boolean isIncident;

    @Column(name = "is_problem", nullable = false)
    private boolean isProblem;

    @Column(name = "is_request", nullable = false)
    private boolean isRequest;

    @Column(name = "is_change", nullable = false)
    private boolean isChange;

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
