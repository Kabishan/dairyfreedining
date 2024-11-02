package com.kabishan.dairyfreedining.network

import com.kabishan.dairyfreedining.BuildKonfig
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.serialization.kotlinx.json.json

val httpClient = HttpClient {
    install(ContentNegotiation) {
        json()
    }

    defaultRequest {
        url(BuildKonfig.BASE_URL)
    }
}