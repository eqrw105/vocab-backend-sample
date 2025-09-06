package domain.di

import domain.service.AppCheckService
import domain.service.TokenService
import org.koin.dsl.module

val domainModule =
    module {
        single<AppCheckService> {
            AppCheckService(
                System.getenv("FIREBASE_PROJECT_NUMBER"),
            )
        }
        single<TokenService> {
            TokenService(
                System.getenv("TOKEN_SECRET").toByteArray(),
                System.getenv("TOKEN_ISSUER"),
            )
        }
    }
