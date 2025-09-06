package application.usecase

import application.model.ApiException
import domain.port.TokenRepository
import java.util.UUID

class RevokeTokenUseCase(
    private val tokenRepository: TokenRepository,
) {
    suspend operator fun invoke(
        tokenId: UUID,
        userId: Long,
    ): Boolean {
        if (userId <= 0) throw ApiException.ValidationException("userId가 0보다 커야합니다.")

        return tokenRepository.revokeRefreshToken(tokenId, userId)
    }
}
