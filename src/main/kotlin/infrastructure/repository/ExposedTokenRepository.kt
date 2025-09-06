package infrastructure.repository

import domain.model.session.RefreshToken
import domain.port.TokenRepository
import infrastructure.table.RefreshTokenDAO
import infrastructure.table.RefreshTokensTable
import infrastructure.table.toRefreshToken
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import java.time.Clock
import java.util.UUID

class ExposedTokenRepository(
    private val clock: Clock = Clock.systemUTC(),
) : TokenRepository {
    override suspend fun getRefreshToken(tokenId: UUID): RefreshToken? =
        transaction {
            RefreshTokenDAO
                .find { RefreshTokensTable.id eq tokenId }
                .limit(1)
                .firstOrNull()
                ?.toRefreshToken()
        }

    override suspend fun createRefreshToken(
        tokenId: UUID,
        userId: Long,
        tokenHash: String,
        expiresAt: Long,
    ): RefreshToken =
        transaction {
            RefreshTokenDAO
                .new(tokenId) {
                    this.userId = userId
                    this.tokenHash = tokenHash
                    this.expiresAt = expiresAt
                    this.createdAt = clock.millis()
                    this.updatedAt = clock.millis()
                }.toRefreshToken()
        }
}
