package plugins.routing.security

import io.ktor.server.routing.Route
import io.ktor.server.auth.authenticate as ktorAuthenticate

@JvmInline
value class AuthName(
    val value: String,
)

object AuthNames {
    val AppCheck: AuthName = AuthName("app-check")
    val Private: AuthName = AuthName("private")
}

object HeaderNames {
    const val APP_CHECK = "X-APP-CHECK-TOKEN"

    const val AUTHORIZATION = "Authorization"
    const val BEARER_TYPE = "bearer"
}

fun Route.authenticate(
    name: AuthName,
    optional: Boolean = false,
    build: Route.() -> Unit,
) = ktorAuthenticate(name.value, optional = optional, build = build)

fun Route.appCheck(build: Route.() -> Unit) = authenticate(name = AuthNames.AppCheck, build = build)

fun Route.private(build: Route.() -> Unit) = authenticate(name = AuthNames.Private, build = build)
