package infrastructure.repository.di

import domain.port.JwkRepository
import infrastructure.repository.DefaultJwkRepository
import org.koin.dsl.module

val repositoryModule =
    module {
        single<JwkRepository> { DefaultJwkRepository(get()) }
    }
