package com.kabishan.dairyfreedining.network

import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.serialization.kotlinx.json.json

private const val BASE_URL = "https://dairyfreedining.koyeb.app/"

val httpClient = HttpClient {
    install(ContentNegotiation) {
        json()
    }

    defaultRequest {
        url(BASE_URL)
    }
}