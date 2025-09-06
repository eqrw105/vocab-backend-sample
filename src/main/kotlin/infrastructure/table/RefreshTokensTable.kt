package infrastructure.table

import domain.model.session.RefreshToken
import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.core.dao.id.UUIDTable
import org.jetbrains.exposed.v1.dao.UUIDEntity
import org.jetbrains.exposed.v1.dao.UUIDEntityClass
import java.util.UUID

// 사용자 세션의 리프레시 토큰을 저장하는 테이블
object RefreshTokensTable : UUIDTable("refresh_tokens") {
    // 소유 사용자 ID(외래키 값)
    val userId = long("user_id").index()

    // 토큰 원문에 대한 안전한 해시값
    val tokenHash = varchar("token_hash", 128)

    // 만료 시각(epoch millis)
    val expiresAt = long("expires_at").index()

    // 생성 시각(epoch millis)
    val createdAt = long("created_at")

    // 마지막 수정 시각(epoch millis)
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
