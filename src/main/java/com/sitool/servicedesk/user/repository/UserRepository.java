package com.sitool.servicedesk.user.repository;

import com.sitool.servicedesk.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Repository for managing {@link User} entities.
 *
 * <p>Provides basic CRUD operations via {@link JpaRepository}
 * and additional query methods for user lookup and validation.</p>
 */
@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    boolean existsByEmail(String email);

    Optional<User> findByEmailIgnoreCase(String email);
    Optional<User> findByEmail(String email);
}
