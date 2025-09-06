package plugins.routing.security

import application.usecase.VerifyAccessTokenUseCase
import application.usecase.VerifyAppCheckTokenUseCase
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.auth.Authentication
import org.koin.ktor.ext.inject

fun Application.configureAuthentication() {
    val verifyAppCheckUseCase: VerifyAppCheckTokenUseCase by inject<VerifyAppCheckTokenUseCase>()
    val verifyAccessTokenUseCase: VerifyAccessTokenUseCase by inject<VerifyAccessTokenUseCase>()
    install(Authentication) {
        appCheck { appCheckToken ->
            verifyAppCheckUseCase(appCheckToken)
        }
        private { accessToken ->
            verifyAccessTokenUseCase(accessToken)
        }
    }
}
