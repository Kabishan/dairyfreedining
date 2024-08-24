package com.kabishan.dairyfreedining.landing

import com.kabishan.dairyfreedining.model.Restaurant
import com.kabishan.dairyfreedining.network.Result
import com.kabishan.dairyfreedining.network.DairyFreeDiningApi
import com.kabishan.dairyfreedining.network.httpClient
import io.ktor.client.call.body
import io.ktor.http.HttpStatusCode

class LandingRepository {
    suspend fun getRestaurants(): Result<List<Restaurant>> {
        try {
            val response = DairyFreeDiningApi(httpClient).getRestaurants()

            return if (response.status == HttpStatusCode.OK) {
                Result.success(response.body())
            } else {
                Result.error(
                    code = response.status.value,
                    message = response.status.description
                )
            }
        } catch (e: Exception) {
            return Result.error(
                code = null,
                message = e.message
            )
        }
    }
}