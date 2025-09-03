import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import plugins.db.configureDatabases
import plugins.di.configureDI
import plugins.exception.configureExceptionHandler
import plugins.routing.configureRouting

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain
        .main(args)
}

fun Application.module() {
    install(ContentNegotiation) {
        json()
    }
    configureDI()
    configureExceptionHandler()
    configureRouting()
    configureDatabases()
}
