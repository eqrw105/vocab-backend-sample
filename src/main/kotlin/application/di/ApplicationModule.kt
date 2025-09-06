package application.di

import application.usecase.CreateGuestTokenUseCase
import application.usecase.VerifyAppCheckTokenUseCase
import org.koin.dsl.module

val applicationModule =
    module {
        single { VerifyAppCheckTokenUseCase(get(), get()) }
        single { CreateGuestTokenUseCase(get(), get(), get()) }
    }
