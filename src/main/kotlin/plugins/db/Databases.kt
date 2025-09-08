package plugins.db

import infrastructure.table.RefreshTokensTable
import infrastructure.table.UsersTable
import infrastructure.table.WordCategoriesTable
import infrastructure.table.WordCategoryMapsTable
import infrastructure.table.WordsTable
import io.ktor.server.application.Application
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.SchemaUtils
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

fun Application.configureDatabases() {
    val url = environment.config.property("ktor.database.url").getString()
    val user = environment.config.property("ktor.database.user").getString()
    val password = environment.config.property("ktor.database.password").getString()
    Database.connect(
        url = url,
        user = user,
        password = password,
    )
    transaction {
        SchemaUtils.create(
            UsersTable,
            RefreshTokensTable,
            WordCategoriesTable,
            WordsTable,
            WordCategoryMapsTable,
        )
    }
}
