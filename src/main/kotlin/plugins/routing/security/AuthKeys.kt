package plugins.routing.security

import io.ktor.server.routing.Route
import io.ktor.server.auth.authenticate as ktorAuthenticate

@JvmInline
value class AuthName(
    val value: String,
)

object AuthNames {
    val AppCheck: AuthName = AuthName("app-check")
}

object HeaderNames {
    const val APP_CHECK = "X-APP-CHECK-TOKEN"
}

fun Route.authenticate(
    name: AuthName,
    optional: Boolean = false,
    build: Route.() -> Unit,
) = ktorAuthenticate(name.value, optional = optional, build = build)

fun Route.appCheck(build: Route.() -> Unit) = authenticate(name = AuthNames.AppCheck, build = build)
