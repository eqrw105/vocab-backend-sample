package domain.port

import domain.model.session.RefreshToken
import java.util.UUID

interface TokenRepository {
    suspend fun createRefreshToken(
        tokenId: UUID,
        userId: Long,
        tokenHash: String,
        expiresAt: Long,
    ): RefreshToken
}
