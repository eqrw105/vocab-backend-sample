package plugins.routing

import application.model.ApiException
import application.usecase.CreateGuestTokenUseCase
import application.usecase.RefreshTokenUseCase
import application.usecase.RevokeTokenUseCase
import io.ktor.http.HttpStatusCode
import io.ktor.resources.Resource
import io.ktor.server.auth.principal
import io.ktor.server.request.receive
import io.ktor.server.resources.delete
import io.ktor.server.resources.post
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.application
import kotlinx.serialization.Serializable
import org.koin.ktor.ext.inject
import plugins.routing.dto.request.SessionRefreshRequestDTO
import plugins.routing.dto.response.TokenDTO
import plugins.routing.dto.response.toDTO
import plugins.routing.security.appCheck
import plugins.routing.security.principal.AppCheckPrincipal
import plugins.routing.security.principal.PrivatePrincipal
import plugins.routing.security.private
import kotlin.uuid.Uuid
import kotlin.uuid.toJavaUuid

@Serializable
@Resource("/v1/session")
class SessionV1 {
    @Serializable
    @Resource("/refresh")
    class Refresh(
        val parent: SessionV1 = SessionV1(),
    )

    @Serializable
    @Resource("/{tokenId}")
    class Id(
        val parent: SessionV1 = SessionV1(),
        val tokenId: Uuid,
    )
}

fun Route.sessionRoutes() {
    appCheck {
        post<SessionV1> {
            val principal = call.principal<AppCheckPrincipal>() ?: throw ApiException.UnauthorizedException()

            val createGuestTokenUseCase by application.inject<CreateGuestTokenUseCase>()
            val result = createGuestTokenUseCase(principal.instanceId)
            call.respond<TokenDTO>(HttpStatusCode.OK, result.toDTO())
        }
    }
    appCheck {
        post<SessionV1.Refresh> {
            val request =
                runCatching { call.receive<SessionRefreshRequestDTO>() }.getOrNull()
                    ?: throw ApiException.ValidationException("요청 파라미터가 올바르지 않습니다.")

            val refreshTokenUseCase by application.inject<RefreshTokenUseCase>()
            val result = refreshTokenUseCase(request.refreshToken)
            call.respond<TokenDTO>(HttpStatusCode.OK, result.toDTO())
        }
    }
    private {
        delete<SessionV1.Id> { route ->
            val principal = call.principal<PrivatePrincipal>() ?: throw ApiException.UnauthorizedException()
            val userId = principal.userId

            val tokenId =
                runCatching { route.tokenId.toJavaUuid() }.getOrNull()
                    ?: throw ApiException.ValidationException("tokenId 형식이 올바르지 않습니다.")

            val revokeTokenUseCase: RevokeTokenUseCase by application.inject<RevokeTokenUseCase>()
            val deleted = revokeTokenUseCase(tokenId, userId)
            if (deleted) {
                call.respond(HttpStatusCode.NoContent)
            } else {
                throw ApiException.NotFoundException()
            }
        }
    }
}
