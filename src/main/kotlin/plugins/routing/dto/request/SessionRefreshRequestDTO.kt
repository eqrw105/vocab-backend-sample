package plugins.routing.dto.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SessionRefreshRequestDTO(
    @SerialName("refreshToken")
    val refreshToken: String,
)
