package com.sitool.servicedesk.token.repository;

import com.sitool.servicedesk.token.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

/**
 * Repository for managing RefreshToken entities.
 *
 * Provides access to refresh tokens for:
 * - authentication validation
 * - token rotation
 * - revocation handling
 */
@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, UUID> {
    /**
     * Finds refresh token entity by its hashed value.
     *
     * @param tokenHash hashed refresh token
     * @return optional RefreshToken entity
     */
    Optional<RefreshToken> findByTokenHash(String tokenHash);

    /**
     * Marks refresh token as revoked in the database using token hash.
     *
     * <p>This operation is used during refresh token rotation to permanently
     * invalidate the previously issued token, preventing any further reuse.
     * The update is executed directly in the database to avoid JPA persistence
     * context side effects and ensure deterministic security behavior.</p>
     */
    @Modifying
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Query("update RefreshToken t set t.revoked = true where t.tokenHash = :tokenHash")
    void revokeByToken(@Param("tokenHash") String tokenHash);
}
