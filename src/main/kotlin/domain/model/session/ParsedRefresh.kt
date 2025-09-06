package domain.model.session

import java.util.UUID

data class ParsedRefresh(
    val tokenId: UUID,
    val secret: String,
)
