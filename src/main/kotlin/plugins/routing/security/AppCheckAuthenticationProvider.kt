package plugins.routing.security

import application.model.ApiException
import io.ktor.server.auth.AuthenticationConfig
import io.ktor.server.auth.AuthenticationContext
import io.ktor.server.auth.AuthenticationProvider
import io.ktor.server.request.header
import plugins.routing.security.principal.AppCheckPrincipal

private class AppCheckAuthenticationProvider(
    private val config: Configuration,
) : AuthenticationProvider(config) {
    class Configuration(
        name: String,
        val headerName: String,
        val verify: suspend (token: String) -> String,
    ) : Config(name)

    override suspend fun onAuthenticate(context: AuthenticationContext) {
        val token = context.call.request.header(config.headerName)
        if (token.isNullOrBlank()) throw ApiException.ValidationException("App Check token이 비어있습니다.")

        val instanceId = config.verify(token)
        context.principal(AppCheckPrincipal(instanceId))
    }
}

fun AuthenticationConfig.appCheck(
    name: String = AuthNames.AppCheck.value,
    headerName: String = HeaderNames.APP_CHECK,
    verify: suspend (token: String) -> String,
) {
    val provider =
        AppCheckAuthenticationProvider(
            AppCheckAuthenticationProvider.Configuration(name, headerName, verify),
        )
    register(provider)
}
