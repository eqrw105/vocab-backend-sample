package application.model

import io.ktor.http.HttpStatusCode
import java.time.LocalDateTime

sealed class ApiException(
    override val message: String,
    val httpStatus: HttpStatusCode,
    val timestamp: LocalDateTime = LocalDateTime.now(),
) : RuntimeException(message) {
    class ValidationException(
        message: String,
    ) : ApiException(
            message = message,
            httpStatus = HttpStatusCode.BadRequest,
        )

    class UnauthorizedException(
        message: String? = null,
    ) : ApiException(
            message = message ?: HttpStatusCode.Unauthorized.description,
            httpStatus = HttpStatusCode.Unauthorized,
        )

    class ForbiddenException(
        message: String? = null,
    ) : ApiException(
            message = message ?: HttpStatusCode.Forbidden.description,
            httpStatus = HttpStatusCode.Forbidden,
        )

    class NotFoundException(
        message: String? = null,
    ) : ApiException(
            message = message ?: HttpStatusCode.NotFound.description,
            httpStatus = HttpStatusCode.NotFound,
        )

    class InternalException(
        message: String? = null,
    ) : ApiException(
            message = message ?: "Internal server error",
            httpStatus = HttpStatusCode.InternalServerError,
        )
}
