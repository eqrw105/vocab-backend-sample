package infrastructure.network.di

import infrastructure.network.api.AppCheckApi
import infrastructure.network.api.AppCheckApiImpl
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.engine.cio.CIO
import io.ktor.client.engine.cio.CIOEngineConfig
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.dsl.module

val networkModule =
    module {
        single<HttpClientEngineFactory<CIOEngineConfig>> { CIO }
        single<HttpClient> {
            HttpClient(get<HttpClientEngineFactory<CIOEngineConfig>>()) {
                expectSuccess = true

                install(ContentNegotiation) {
                    json(
                        Json {
                            ignoreUnknownKeys = true
                            explicitNulls = false
                        },
                    )
                }
            }
        }
        single<AppCheckApi> { AppCheckApiImpl(get()) }
    }
