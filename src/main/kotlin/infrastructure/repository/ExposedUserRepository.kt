package infrastructure.repository

import domain.model.user.User
import domain.model.user.UserStatus
import domain.model.user.UserType
import domain.port.UserRepository
import infrastructure.table.UserDAO
import infrastructure.table.UsersTable
import infrastructure.table.toUser
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

class ExposedUserRepository : UserRepository {
    override suspend fun getUser(userId: Long): User? =
        transaction {
            UserDAO
                .find { UsersTable.id eq userId }
                .limit(1)
                .firstOrNull()
                ?.toUser()
        }

    override suspend fun getUserByInstanceId(instanceId: String): User? =
        transaction {
            UserDAO
                .find { UsersTable.instanceId eq instanceId }
                .limit(1)
                .firstOrNull()
                ?.toUser()
        }

    override suspend fun createGuest(
        instanceId: String,
        status: UserStatus,
        type: UserType,
    ): User =
        transaction {
            UserDAO
                .new {
                    this.instanceId = instanceId
                    this.status = status
                    this.type = type
                }.toUser()
        }
}
