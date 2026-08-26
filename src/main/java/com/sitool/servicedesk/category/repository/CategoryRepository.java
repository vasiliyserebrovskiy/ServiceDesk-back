package com.sitool.servicedesk.category.repository;

import com.sitool.servicedesk.category.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Spring Data repository for managing {@link Category} entities.
 * Provides CRUD operations and query methods for Category persistence.
 */
public interface CategoryRepository extends JpaRepository<Category, UUID> {

    boolean existsByNameIgnoreCase(String name);
    List<Category> findAllByIsIncidentTrue();
    List<Category> findAllByIsProblemTrue();
    List<Category> findAllByIsRequestTrue();
    List<Category> findAllByIsChangeTrue();

    @Query("select c.id from Category c where c.name = :name")
    Optional<UUID> findIdByName(@Param("name") String name);
}
