package domain.model.session

import java.util.UUID

data class RefreshToken(
    val tokenId: UUID,
    val userId: Long,
    val tokenHash: String,
    val expiresAt: Long,
    val createdAt: Long,
    val updatedAt: Long,
)
