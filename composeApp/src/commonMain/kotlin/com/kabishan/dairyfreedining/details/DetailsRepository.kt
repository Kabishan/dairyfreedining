package com.kabishan.dairyfreedining.details

import com.kabishan.dairyfreedining.model.RestaurantDetails
import io.ktor.client.call.body
import io.ktor.http.HttpStatusCode
import com.kabishan.dairyfreedining.network.Result
import com.kabishan.dairyfreedining.network.DairyFreeDiningApi
import com.kabishan.dairyfreedining.network.httpClient

class DetailsRepository {
    suspend fun getRestaurantDetails(restaurantId: String): Result<RestaurantDetails> {
        try {
            val response = DairyFreeDiningApi(httpClient).getRestaurantDetails(restaurantId)

            return if (response.status == HttpStatusCode.OK) {
                Result.success(data = response.body())
            } else {
                Result.error(
                    code = response.status.value,
                    message = response.status.description
                )
            }
        } catch(e: Exception) {
            return Result.error(
                code = null,
                message = e.message
            )
        }
    }
}