package plugins.routing.dto.response

import domain.model.session.Token
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.uuid.Uuid
import kotlin.uuid.toKotlinUuid

@Serializable
data class TokenDTO(
    @SerialName("tokenId")
    val tokenId: Uuid,
    @SerialName("accessToken")
    val accessToken: String,
    @SerialName("accessTokenExpiredAt")
    val accessTokenExpiredAt: Long,
    @SerialName("refreshToken")
    val refreshToken: String,
    @SerialName("refreshTokenExpiredAt")
    val refreshTokenExpiredAt: Long,
)

fun Token.toDTO(): TokenDTO =
    TokenDTO(
        tokenId = refreshTokenId.toKotlinUuid(),
        accessToken = accessToken,
        accessTokenExpiredAt = accessTokenExpiresAt,
        refreshToken = refreshToken,
        refreshTokenExpiredAt = refreshTokenExpiresAt,
    )
