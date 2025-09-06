package infrastructure.network.api

import infrastructure.network.model.response.JwksResponse

interface AppCheckApi {
    suspend fun getJwks(): JwksResponse?
}
