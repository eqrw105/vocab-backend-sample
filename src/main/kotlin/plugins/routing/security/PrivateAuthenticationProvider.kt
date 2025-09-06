package plugins.routing.security

import application.model.ApiException
import io.ktor.server.auth.AuthenticationConfig
import io.ktor.server.auth.AuthenticationContext
import io.ktor.server.auth.AuthenticationProvider
import io.ktor.server.request.header
import plugins.routing.security.principal.PrivatePrincipal

private class PrivateAuthenticationProvider(
    private val config: Configuration,
) : AuthenticationProvider(config) {
    class Configuration(
        name: String,
        val headerName: String,
        val verify: suspend (token: String) -> Long,
    ) : Config(name)

    override suspend fun onAuthenticate(context: AuthenticationContext) {
        val raw = context.call.request.header(config.headerName)

        if (raw.isNullOrBlank()) throw ApiException.ValidationException("Access token이 비어있습니다.")

        val token =
            raw
                .takeIf { it.startsWith(HeaderNames.BEARER_TYPE, ignoreCase = true) }
                ?.substringAfter(HeaderNames.BEARER_TYPE)
                ?.trim()

        if (token.isNullOrBlank()) throw ApiException.ValidationException("Access token 형식이 올바르지 않습니다.")

        val userId = config.verify(token)
        context.principal(PrivatePrincipal(userId))
    }
}

fun AuthenticationConfig.private(
    name: String = AuthNames.Private.value,
    headerName: String = HeaderNames.AUTHORIZATION,
    verify: suspend (token: String) -> Long,
) {
    val provider =
        PrivateAuthenticationProvider(
            PrivateAuthenticationProvider.Configuration(name, headerName, verify),
        )
    register(provider)
}
