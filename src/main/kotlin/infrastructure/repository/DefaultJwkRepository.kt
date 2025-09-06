package infrastructure.repository

import domain.port.JwkRepository
import infrastructure.network.api.AppCheckApi
import java.math.BigInteger
import java.security.KeyFactory
import java.security.interfaces.RSAPublicKey
import java.security.spec.RSAPublicKeySpec
import java.util.Base64

class DefaultJwkRepository(
    private val appCheckApi: AppCheckApi,
) : JwkRepository {
    override suspend fun getPublicKeyMap(): Map<String, RSAPublicKey> {
        val jwks = appCheckApi.getJwks() ?: return emptyMap()

        return jwks.keys
            .filter {
                it.kty == "RSA" && it.n != null && it.e != null && it.kid != null
            }.associate { jwk ->
                val pub = buildRsaPublicKey(jwk.n!!, jwk.e!!)
                jwk.kid!! to pub
            }
    }

    private fun buildRsaPublicKey(
        nB64Url: String,
        eB64Url: String,
    ): RSAPublicKey {
        val nBytes = base64UrlDecode(nB64Url)
        val eBytes = base64UrlDecode(eB64Url)
        val modulus = BigInteger(1, nBytes)
        val exponent = BigInteger(1, eBytes)
        val spec = RSAPublicKeySpec(modulus, exponent)
        val kf = KeyFactory.getInstance("RSA")
        return kf.generatePublic(spec) as RSAPublicKey
    }

    private fun base64UrlDecode(s: String): ByteArray {
        val padded =
            when (s.length % 4) {
                2 -> "$s=="
                3 -> "$s="
                else -> s
            }
        return Base64.getUrlDecoder().decode(padded)
    }
}
