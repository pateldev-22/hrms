package com.hrms.hrms_backend.service;

import com.hrms.hrms_backend.entity.RefreshToken;
import com.hrms.hrms_backend.entity.User;
import com.hrms.hrms_backend.repository.RefreshTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * RefreshTokenService - Manages refresh tokens
 *
 * Responsibilities:
 * 1. Create new refresh tokens
 * 2. Verify refresh tokens
 * 3. Delete expired tokens
 * 4. Revoke user's tokens (logout)
 *
 * @Transactional: Ensures database operations are atomic
 * If anything fails, all changes are rolled back
 */
@Service
public class RefreshTokenService {

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    /**
     * Refresh token expiration from application.properties
     * Default: 604800000 ms = 7 days
     */
    @Value("${jwt.refresh-token-expiration}")
    private Long refreshTokenExpiration;

    /**
     * Create a new refresh token for user
     *
     * Steps:
     * 1. Generate random UUID token
     * 2. Calculate expiration time (now + 7 days)
     * 3. Create RefreshToken entity
     * 4. Save to database
     * 5. Return the token
     *
     * @param user The user to create token for
     * @return RefreshToken entity
     *
     * UUID.randomUUID():
     * - Generates unique ID like "a1b2c3d4-e5f6-7890-abcd-ef1234567890"
     * - Extremely unlikely to have collisions (duplicates)
     * - More secure than sequential IDs
     */
    public RefreshToken createRefreshToken(User user) {
        RefreshToken refreshToken = RefreshToken.builder()
                .user(user)
                .token(UUID.randomUUID().toString())
                .expiryDate(Instant.now().plusMillis(refreshTokenExpiration))
                .createdAt(Instant.now())
                .build();

        return refreshTokenRepository.save(refreshToken);
    }

    /**
     * Find refresh token by token string
     *
     * @param token The token string
     * @return Optional containing RefreshToken if found
     */
    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    /**
     * Verify if refresh token is valid
     *
     * Checks if token is expired
     * If expired, deletes it from database
     *
     * @param token RefreshToken entity
     * @return The same token if valid
     * @throws RuntimeException if token is expired
     *
     * Why delete expired tokens?
     * - Keeps database clean
     * - Prevents reuse of expired tokens
     * - Security best practice
     */
    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.isExpired()) {
            refreshTokenRepository.delete(token);
            throw new RuntimeException(
                    "Refresh token was expired. Please make a new login request"
            );
        }
        return token;
    }

    /**
     * Delete all refresh tokens for a user
     *
     * Used for:
     * - Logout (invalidate current session)
     * - Logout from all devices
     * - User changes password (revoke all sessions)
     *
     * @Transactional: Ensures delete operation is committed
     *
     * @param user The user whose tokens to delete
     */
    @Transactional
    public void deleteByUser(User user) {
        refreshTokenRepository.deleteByUser(user);
    }

    /**
     * Delete expired refresh tokens
     *
     * Should be run as scheduled task (e.g., daily)
     * Cleans up old tokens from database
     *
     * @Transactional: Ensures delete is committed
     */
    @Transactional
    public void deleteExpiredTokens() {
        refreshTokenRepository.deleteExpiredTokens();
    }
}
