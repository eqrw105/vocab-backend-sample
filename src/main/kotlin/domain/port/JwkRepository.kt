package domain.port

import java.security.interfaces.RSAPublicKey

interface JwkRepository {
    suspend fun getPublicKeyMap(): Map<String, RSAPublicKey>
}
