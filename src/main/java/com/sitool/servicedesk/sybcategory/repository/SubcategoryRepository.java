package com.sitool.servicedesk.sybcategory.repository;


import com.sitool.servicedesk.sybcategory.entity.Subcategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Spring Data repository for managing {@link Subcategory} entities.
 * Provides CRUD operations and query methods for Subcategory persistence.
 */
public interface SubcategoryRepository extends JpaRepository<Subcategory, UUID> {
    boolean existsByNameIgnoreCase(String name);

    List<Subcategory> findAllByCategoryId(UUID categoryId);
    Optional<UUID> findIdByName(String name);
}
