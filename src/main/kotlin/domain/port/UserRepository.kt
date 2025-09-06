package domain.port

import domain.model.user.User
import domain.model.user.UserStatus
import domain.model.user.UserType

interface UserRepository {
    suspend fun getUser(userId: Long): User?

    suspend fun getUserByInstanceId(instanceId: String): User?

    suspend fun createGuest(
        instanceId: String,
        status: UserStatus,
        type: UserType,
    ): User
}
