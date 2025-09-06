package application.usecase

import application.model.ApiException
import domain.service.TokenService

class VerifyAccessTokenUseCase(
    private val tokenService: TokenService,
) {
    operator fun invoke(accessToken: String): Long {
        if (accessToken.isBlank()) throw ApiException.ValidationException("accessToken이 비어있습니다.")

        val decodedToken =
            tokenService.verifyAccessToken(accessToken) ?: throw ApiException.UnauthorizedException("토큰이 만료되었습니다.")

        return decodedToken.subject.toLong()
    }
}
