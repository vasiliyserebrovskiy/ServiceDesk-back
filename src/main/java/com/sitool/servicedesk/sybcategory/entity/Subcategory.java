package com.sitool.servicedesk.sybcategory.entity;

import com.sitool.servicedesk.category.entity.Category;
import com.sitool.servicedesk.utils.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * Represents a subcategory within a service desk category.
 *
 * <p>Subcategories provide a second level of classification for tickets,
 * allowing more precise grouping within a parent {@link Category}.</p>
 *
 * <p>Examples:
 * <ul>
 *     <li>Hardware Issue → Laptop, Monitor, Printer</li>
 *     <li>Software → OS, Application, Browser</li>
 * </ul>
 * </p>
 *
 * <p>The entity is mapped to the {@code subcategories} database table.</p>
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "subcategories")
public class Subcategory extends BaseEntity {

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    @UpdateTimestamp
    private LocalDateTime updatedAt;


    @Override
    public String toString() {
        return "Subcategory {" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
