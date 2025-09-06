package infrastructure.network.api

import infrastructure.network.api.AppCheckApi
import infrastructure.network.model.response.JwksResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.accept
import io.ktor.client.request.get
import io.ktor.http.ContentType

internal class AppCheckApiImpl(
    private val httpClient: HttpClient,
    private val url: String = "https://firebaseappcheck.googleapis.com/v1/jwks",
) : AppCheckApi {
    override suspend fun getJwks(): JwksResponse? =
        try {
            httpClient
                .get(url) {
                    accept(ContentType.Application.Json)
                }.body()
        } catch (_: Exception) {
            null
        }
}
