package com.donai.app.core.network

import io.ktor.client.*
import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

fun createHttpClient(
    engine: HttpClientEngineFactory<*>
): HttpClient {

    return HttpClient(engine) {

        install(ContentNegotiation) {
            json(
                Json {
                    ignoreUnknownKeys = true
                    prettyPrint = true
                    isLenient = true
                }
            )
        }
    }
}