package com.sitool.servicedesk.category.repository;

import com.sitool.servicedesk.category.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

/**
 * Spring Data repository for managing {@link Category} entities.
 * Provides CRUD operations and query methods for Category persistence.
 */
public interface CategoryRepository extends JpaRepository<Category, UUID> {
}
