package com.sitool.servicedesk.user.repository;

import com.sitool.servicedesk.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Repository for managing {@link User} entities.
 *
 * <p>Provides CRUD operations and custom query methods
 * for user lookup and validation.</p>
 */
@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    /**
     * Checks whether a user with the given email already exists.
     *
     * @param email user email
     * @return true if a user exists, otherwise false
     */
    boolean existsByEmail(String email);

    /**
     * Finds a user by email ignoring letter case.
     *
     * @param email user email
     * @return optional containing the found user
     */
    Optional<User> findByEmailIgnoreCase(String email);

    /**
     * Finds a user by exact email match.
     *
     * @param email user email
     * @return optional containing the found user
     */
    Optional<User> findByEmail(String email);
}
