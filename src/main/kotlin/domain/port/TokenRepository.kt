package domain.port

import domain.model.session.RefreshToken
import java.util.UUID

interface TokenRepository {
    suspend fun getRefreshToken(tokenId: UUID): RefreshToken?

    suspend fun createRefreshToken(
        tokenId: UUID,
        userId: Long,
        tokenHash: String,
        expiresAt: Long,
    ): RefreshToken

    suspend fun replaceRefreshToken(
        userId: Long,
        oldTokenId: UUID,
        newTokenId: UUID,
        newTokenHash: String,
        newExpiresAt: Long,
    ): Boolean
}
