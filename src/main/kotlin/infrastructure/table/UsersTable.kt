package infrastructure.table

import domain.model.AuthProvider
import domain.model.UserStatus
import domain.model.UserType
import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.core.dao.id.LongIdTable
import org.jetbrains.exposed.v1.dao.LongEntity
import org.jetbrains.exposed.v1.dao.LongEntityClass
import org.jetbrains.exposed.v1.javatime.datetime
import java.time.LocalDateTime

object UsersTable : LongIdTable("users") {
    val email = varchar("email", 255).uniqueIndex().nullable()
    val username = varchar("username", 50).uniqueIndex().nullable()
    val passwordHash = text("password_hash").nullable()
    val socialId = varchar("social_id", 255).nullable()
    val provider = enumerationByName("provider", 20, AuthProvider::class).nullable()
    val status = enumerationByName("status", 20, UserStatus::class).nullable()
    val type = enumerationByName("type", 20, UserType::class).default(UserType.Guest)
    val createdAt = datetime("created_at").default(LocalDateTime.now())
    val updatedAt = datetime("updated_at").default(LocalDateTime.now())
    val deletedAt = datetime("deleted_at").nullable()
}

class UserDAO(
    id: EntityID<Long>,
) : LongEntity(id) {
    companion object : LongEntityClass<UserDAO>(UsersTable)

    val email by UsersTable.email
    val username by UsersTable.username
    val passwordHash by UsersTable.passwordHash
    val socialId by UsersTable.socialId
    val provider by UsersTable.provider
    val status by UsersTable.status
    val type by UsersTable.type
    val createdAt by UsersTable.createdAt
    val updatedAt by UsersTable.updatedAt
    val deletedAt by UsersTable.deletedAt
}
