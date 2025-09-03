package infrastructure.table

import domain.model.session.RefreshToken
import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.core.dao.id.UUIDTable
import org.jetbrains.exposed.v1.dao.UUIDEntity
import org.jetbrains.exposed.v1.dao.UUIDEntityClass
import java.util.UUID

object RefreshTokensTable : UUIDTable("refresh_tokens") {
    val userId = long("user_id").index()
    val tokenHash = varchar("token_hash", 128)
    val expiresAt = long("expires_at").index()
    val createdAt = long("created_at")
    val updatedAt = long("updated_at")
}

class RefreshTokenDAO(
    id: EntityID<UUID>,
) : UUIDEntity(id) {
    companion object : UUIDEntityClass<RefreshTokenDAO>(RefreshTokensTable)

    var userId by RefreshTokensTable.userId
    var tokenHash by RefreshTokensTable.tokenHash
    var expiresAt by RefreshTokensTable.expiresAt
    var createdAt by RefreshTokensTable.createdAt
    var updatedAt by RefreshTokensTable.updatedAt
}

fun RefreshTokenDAO.toRefreshToken(): RefreshToken =
    RefreshToken(
        tokenId = id.value,
        userId = userId,
        tokenHash = tokenHash,
        expiresAt = expiresAt,
        createdAt = createdAt,
        updatedAt = updatedAt,
    )
