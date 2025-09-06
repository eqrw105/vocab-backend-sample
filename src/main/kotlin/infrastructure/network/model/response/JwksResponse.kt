package infrastructure.network.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class JwksResponse(
    @SerialName("keys")
    val keys: List<Jwk>,
) {
    @Serializable
    data class Jwk(
        @SerialName("kty")
        val kty: String,
        @SerialName("use")
        val use: String? = null,
        @SerialName("alg")
        val alg: String? = null,
        @SerialName("kid")
        val kid: String? = null,
        @SerialName("n")
        val n: String? = null,
        @SerialName("e")
        val e: String? = null,
        @SerialName("x5c")
        val x5c: List<String>? = null,
    )
}
