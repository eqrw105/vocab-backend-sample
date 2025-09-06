package domain.model.session

data class DecodedToken(
    val subject: String,
    val claims: Map<String, Any?>,
    val expiresAt: Long,
)
