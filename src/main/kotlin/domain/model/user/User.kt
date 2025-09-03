package domain.model.user

import domain.model.auth.AuthProvider
import domain.model.user.UserStatus
import domain.model.user.UserType
import java.time.LocalDateTime

data class User(
    val id: Long,
    val email: String?,
    val name: String?,
    val passwordHash: String?,
    val socialId: String?,
    val instanceId: String?,
    val provider: AuthProvider?,
    val status: UserStatus?,
    val type: UserType,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val deletedAt: LocalDateTime?,
)
