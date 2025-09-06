package application.usecase

import application.model.ApiException
import domain.port.JwkRepository
import domain.service.AppCheckService

class VerifyAppCheckTokenUseCase(
    private val jwkRepository: JwkRepository,
    private val appCheckService: AppCheckService,
) {
    suspend operator fun invoke(token: String): String {
        if (token.isBlank()) throw ApiException.ValidationException("token이 비어있습니다.")
        val publicKeyMap = jwkRepository.getPublicKeyMap()
        return appCheckService.verifyAndExtract(token, publicKeyMap)
    }
}
