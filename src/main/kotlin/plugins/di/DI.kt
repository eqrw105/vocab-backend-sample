package plugins.di

import domain.di.domainModule
import infrastructure.network.di.networkModule
import infrastructure.repository.di.repositoryModule
import io.ktor.server.application.Application
import io.ktor.server.application.install
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger

fun Application.configureDI() {
    install(Koin) {
        slf4jLogger()
        modules(
            domainModule,
            networkModule,
            repositoryModule,
        )
    }
}
