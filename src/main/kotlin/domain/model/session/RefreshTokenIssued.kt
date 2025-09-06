package domain.model.session

import java.util.UUID

// TokenService가 발급 시 반환하는 리프레시 내부 DTO(DB 저장용 정보 포함)
data class RefreshTokenIssued(
    val id: UUID, // DB 조회 키
    val raw: String, // 클라이언트에 내려갈 원문 "tokenId.secret"
    val expiresAt: Long, // epoch millis
    val secretHash: String, // DB에 저장할 해시(SHA-256 base64url)
)
