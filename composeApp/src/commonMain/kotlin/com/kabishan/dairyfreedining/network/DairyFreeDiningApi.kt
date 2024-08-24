package com.kabishan.dairyfreedining.network

import com.kabishan.dairyfreedining.model.Submission
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.contentType

class DairyFreeDiningApi(
    private val httpClient: HttpClient
) {
    suspend fun getRestaurants(): HttpResponse {
        return httpClient.get(RESTAURANT_URL)
    }

    suspend fun getRestaurantDetails(restaurantId: String): HttpResponse {
        return httpClient.get(RESTAURANT_DETAILS_URL.replace(RESTAURANT_ID_TEMPLATE, restaurantId))
    }

    suspend fun submitFood(
        submission: Submission
    ): HttpResponse {
        return httpClient.post(SUBMIT_URL) {
            contentType(ContentType.Application.Json)
            setBody(submission)
        }
    }

    companion object {
        private const val RESTAURANT_URL = "restaurants"

        private const val RESTAURANT_ID_TEMPLATE = "{restaurantId}"
        private const val RESTAURANT_DETAILS_URL = "$RESTAURANT_URL/$RESTAURANT_ID_TEMPLATE"

        private const val SUBMIT_URL = "submit"
    }
}