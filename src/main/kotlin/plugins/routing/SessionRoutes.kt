package plugins.routing

import application.model.ApiException
import application.usecase.CreateGuestTokenUseCase
import io.ktor.http.HttpStatusCode
import io.ktor.resources.Resource
import io.ktor.server.auth.principal
import io.ktor.server.resources.post
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.application
import kotlinx.serialization.Serializable
import org.koin.ktor.ext.inject
import plugins.routing.dto.response.TokenDTO
import plugins.routing.dto.response.toDTO
import plugins.routing.security.appCheck
import plugins.routing.security.principal.AppCheckPrincipal

@Serializable
@Resource("/v1/session")
class SessionV1

fun Route.sessionRoutes() {
    appCheck {
        post<SessionV1> {
            val principal = call.principal<AppCheckPrincipal>() ?: throw ApiException.UnauthorizedException()

            val createGuestTokenUseCase by application.inject<CreateGuestTokenUseCase>()
            val result = createGuestTokenUseCase(principal.instanceId)
            call.respond<TokenDTO>(HttpStatusCode.OK, result.toDTO())
        }
    }
}
