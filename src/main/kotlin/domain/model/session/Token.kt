package domain.model.session

import java.util.UUID

data class Token(
    val accessToken: String,
    val accessTokenExpiresAt: Long,
    val refreshTokenId: UUID,
    val refreshToken: String,
    val refreshTokenExpiresAt: Long,
)
