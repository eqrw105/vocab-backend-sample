package infrastructure.repository.di

import domain.port.JwkRepository
import domain.port.TokenRepository
import domain.port.UserRepository
import infrastructure.repository.DefaultJwkRepository
import infrastructure.repository.ExposedTokenRepository
import infrastructure.repository.ExposedUserRepository
import org.koin.dsl.module

val repositoryModule =
    module {
        single<JwkRepository> { DefaultJwkRepository(get()) }
        single<UserRepository> { ExposedUserRepository() }
        single<TokenRepository> { ExposedTokenRepository() }
    }
