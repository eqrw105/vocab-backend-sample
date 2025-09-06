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

// 사용자 계정 및 인증 정보를 저장하는 테이블
object UsersTable : LongIdTable("users") {
    // 이메일(고유)
    val email = varchar("email", 255).uniqueIndex().nullable()

    // 사용자 표시 이름/닉네임(고유)
    val name = varchar("name", 50).uniqueIndex().nullable()

    // 비밀번호 해시(로그인 시 사용)
    val passwordHash = text("password_hash").nullable()

    // 소셜 로그인 제공자의 사용자 식별자(고유)
    val socialId = varchar("social_id", 255).uniqueIndex().nullable()

    // 클라이언트/인스턴스 식별자(고유)
    val instanceId = varchar("instance_id", 255).uniqueIndex().nullable()

    // 인증 제공자(enum)
    val provider = enumerationByName("provider", 20, AuthProvider::class).nullable()

    // 계정 상태(enum)
    val status = enumerationByName("status", 20, UserStatus::class).nullable()

    // 사용자 유형(enum)
    val type = enumerationByName("type", 20, UserType::class).default(UserType.Guest)

    // 생성 시각
    val createdAt = datetime("created_at").default(LocalDateTime.now())

    // 마지막 수정 시각
    val updatedAt = datetime("updated_at").default(LocalDateTime.now())
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
    )
