package application.usecase

import application.model.ApiException
import domain.model.session.Token
import domain.model.user.UserStatus
import domain.model.user.UserType
import domain.port.TokenRepository
import domain.port.UserRepository
import domain.service.TokenService

class CreateGuestTokenUseCase(
    private val userRepository: UserRepository,
    private val tokenRepository: TokenRepository,
    private val tokenService: TokenService,
) {
    suspend operator fun invoke(instanceId: String): Token {
        if (instanceId.isBlank()) throw ApiException.ValidationException("instanceId가 비어있습니다.")

        val existing = userRepository.getUserByInstanceId(instanceId)
        val user = existing ?: userRepository.createGuest(instanceId, UserStatus.Active, UserType.Guest)

        val accessToken =
            tokenService.issueAccessToken(
                subject = user.id.toString(),
                claims = mapOf("type" to user.type.name),
                ttlSeconds = TokenService.ACCESS_TOKEN_TTL.inWholeSeconds,
            )

        val refreshToken =
            tokenService.issueRefreshToken(
                ttlSeconds = TokenService.REFRESH_TOKEN_TTL.inWholeSeconds,
            )

        tokenRepository.createRefreshToken(
            tokenId = refreshToken.id,
            userId = user.id,
            tokenHash = refreshToken.secretHash,
            expiresAt = refreshToken.expiresAt,
        )

        return Token(
            accessToken = accessToken.value,
            accessTokenExpiresAt = accessToken.expiresAt,
            refreshTokenId = refreshToken.id,
            refreshToken = refreshToken.raw,
            refreshTokenExpiresAt = refreshToken.expiresAt,
        )
    }
}
