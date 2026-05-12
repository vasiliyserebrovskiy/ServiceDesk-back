package com.sitool.servicedesk.token.entity;

import com.sitool.servicedesk.utils.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

/**
 * Entity representing refresh token stored in database.
 *
 * Used for:
 * - JWT refresh token rotation
 * - Session tracking per user/device
 * - Token revocation support
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "refresh_tokens")
public class RefreshToken extends BaseEntity {

    @Column(name = "user_id", nullable = false)
    private UUID userId; // owner of refresh token

    @Column(name = "token_hash", nullable = false, unique = true, length = 255)
    private String tokenHash; // hashed refresh token (not raw JWT)

    @Column(name = "created_at", nullable = false)
    private Instant createdAt; // token creation time

    @Column(name = "expires_at", nullable = false)
    private Instant expiresAt; // token expiration time

    @Column(name = "revoked", nullable = false)
    private boolean revoked = false; // token invalidation flag

    @Column(name = "device_info")
    private String deviceInfo; // optional device metadata. Not used for now.


    @Override
    public String toString() {
        return "RefreshToken {" +
                "id=" + id +
                ", userId=" + userId +
                ", tokenHash='" + tokenHash + '\'' +
                ", createdAt=" + createdAt +
                ", expiresAt=" + expiresAt +
                ", revoked=" + revoked +
                ", deviceInfo='" + deviceInfo + '\'' +
                '}';
    }
}
