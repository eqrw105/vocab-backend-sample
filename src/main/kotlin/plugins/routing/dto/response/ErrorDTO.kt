package plugins.routing.dto.response

import application.model.ApiException
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.toKotlinLocalDateTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ErrorDTO(
    @SerialName("message")
    val message: String,
    @SerialName("timestamp")
    val timestamp: LocalDateTime,
)

fun ApiException.toErrorDTO() =
    ErrorDTO(
        message = message,
        timestamp = timestamp.toKotlinLocalDateTime(),
    )
