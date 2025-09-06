package plugins.routing.dto.response

import domain.model.auth.AuthProvider
import domain.model.user.User
import domain.model.user.UserStatus
import domain.model.user.UserType
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.toKotlinLocalDateTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class UserDTO(
    @SerialName("id")
    val id: Long,
    @SerialName("email")
    val email: String?,
    @SerialName("name")
    val name: String?,
    @SerialName("socialId")
    val socialId: String?,
    @SerialName("instanceId")
    val instanceId: String?,
    @SerialName("provider")
    val provider: AuthProvider?,
    @SerialName("status")
    val status: UserStatus?,
    @SerialName("type")
    val type: UserType,
    @SerialName("createdAt")
    val createdAt: LocalDateTime,
    @SerialName("updatedAt")
    val updatedAt: LocalDateTime,
)

fun User.toDTO() =
    UserDTO(
        id = id,
        email = email,
        name = name,
        socialId = socialId,
        instanceId = instanceId,
        provider = provider,
        status = status,
        type = type,
        createdAt = createdAt.toKotlinLocalDateTime(),
        updatedAt = updatedAt.toKotlinLocalDateTime(),
    )
