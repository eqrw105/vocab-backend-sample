package plugins.exception

import application.model.ApiException
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.statuspages.StatusPages
import io.ktor.server.response.respond
import plugins.routing.dto.response.ErrorDTO
import plugins.routing.dto.response.toErrorDTO

fun Application.configureExceptionHandler() {
    install(StatusPages) {
        exception<ApiException> { call, cause ->
            call.respond<ErrorDTO>(status = cause.httpStatus, message = cause.toErrorDTO())
        }
        exception<Throwable> { call, cause ->
            val apiException = ApiException.InternalException(cause.message)
            call.respond<ErrorDTO>(
                status = apiException.httpStatus,
                message = apiException.toErrorDTO(),
            )
        }
    }
}
