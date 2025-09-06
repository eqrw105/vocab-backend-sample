package domain.model.session

data class AccessTokenIssued(
    val value: String,
    val expiresAt: Long, // epoch millis (UTC)
)
