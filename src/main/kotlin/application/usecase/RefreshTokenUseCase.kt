package application.usecase

import application.model.ApiException
import domain.model.session.Token
import domain.port.TokenRepository
import domain.port.UserRepository
import domain.service.TokenService
import java.time.Clock
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.hours

class RefreshTokenUseCase(
    private val userRepository: UserRepository,
    private val tokenRepository: TokenRepository,
    private val tokenService: TokenService,
    private val clock: Clock = Clock.systemUTC(),
) {
    suspend operator fun invoke(refreshToken: String): Token {
        val parsed =
            tokenService.parseRefreshToken(refreshToken)
                ?: throw ApiException.ValidationException("리프레시 토큰 형식이 올바르지 않습니다.")
        val record =
            tokenRepository.getRefreshToken(parsed.tokenId)
                ?: throw ApiException.ForbiddenException("리프레시 토큰을 찾을 수 없습니다.")

        val now =
            java.time.Instant
                .now(clock)
                .toEpochMilli()
        if (record.expiresAt <= now) throw ApiException.ForbiddenException("리프레시 토큰이 만료되었습니다.")

        val providedHash = tokenService.hashRefreshSecret(parsed.secret)
        val ok = tokenService.constantTimeEquals(providedHash, record.tokenHash)
        if (!ok) throw ApiException.ForbiddenException("리프레시 토큰이 유효하지 않습니다.")

        val user =
            userRepository.getUser(record.userId) ?: throw ApiException.ForbiddenException("유저 정보가 존재하지 않습니다.")

        // 새 액세스 토큰 발급
        val accessToken =
            tokenService.issueAccessToken(
                subject = record.userId.toString(),
                claims = mapOf("type" to user.type.name),
                ttlSeconds = accessTokenTTL.inWholeSeconds,
            )

        val refreshToken =
            tokenService.issueRefreshToken(
                ttlSeconds = refreshTokenTTL.inWholeSeconds,
            )

        val replaced =
            tokenRepository.replaceRefreshToken(
                oldTokenId = record.tokenId,
                newTokenId = refreshToken.id,
                userId = record.userId,
                newTokenHash = refreshToken.secretHash,
                newExpiresAt = refreshToken.expiresAt,
            )

        // 경쟁 상태 또는 중복 사용 탐지 등으로 실패
        if (!replaced) throw ApiException.InternalException("리프레시 토큰 교체에 실패했습니다.")

        return Token(
            accessToken = accessToken.value,
            accessTokenExpiresAt = accessToken.expiresAt,
            refreshTokenId = refreshToken.id,
            refreshToken = refreshToken.raw,
            refreshTokenExpiresAt = refreshToken.expiresAt,
        )
    }

    companion object Companion {
        private val accessTokenTTL = 1.hours
        private val refreshTokenTTL = 30.days
    }
}
