package domain.di

import domain.service.AppCheckService
import org.koin.dsl.module

val domainModule =
    module {
        single<AppCheckService> {
            AppCheckService(
                System.getenv("FIREBASE_PROJECT_NUMBER"),
            )
        }
    }
