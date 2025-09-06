package infrastructure.table

import domain.model.auth.AuthProvider
import domain.model.user.User
import domain.model.user.UserStatus
import domain.model.user.UserType
import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.core.dao.id.LongIdTable
import org.jetbrains.exposed.v1.dao.LongEntity
import org.jetbrains.exposed.v1.dao.LongEntityClass
import org.jetbrains.exposed.v1.javatime.datetime
import java.time.LocalDateTime

object UsersTable : LongIdTable("users") {
    val email = varchar("email", 255).uniqueIndex().nullable()
    val name = varchar("name", 50).uniqueIndex().nullable()
    val passwordHash = text("password_hash").nullable()
    val socialId = varchar("social_id", 255).uniqueIndex().nullable()
    val instanceId = varchar("instance_id", 255).uniqueIndex().nullable()
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

    var email by UsersTable.email
    var name by UsersTable.name
    var passwordHash by UsersTable.passwordHash
    var socialId by UsersTable.socialId
    var instanceId by UsersTable.instanceId
    var provider by UsersTable.provider
    var status by UsersTable.status
    var type by UsersTable.type
    var createdAt by UsersTable.createdAt
    var updatedAt by UsersTable.updatedAt
    var deletedAt by UsersTable.deletedAt
}

fun UserDAO.toUser(): User =
    User(
        id = id.value,
        email = email,
        name = name,
        passwordHash = passwordHash,
        socialId = socialId,
        instanceId = instanceId,
        provider = provider,
        status = status,
        type = type,
        createdAt = createdAt,
        updatedAt = updatedAt,
        deletedAt = deletedAt,
    )
