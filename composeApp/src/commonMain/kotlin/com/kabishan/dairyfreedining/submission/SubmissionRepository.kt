package com.kabishan.dairyfreedining.submission

import com.kabishan.dairyfreedining.model.Submission
import com.kabishan.dairyfreedining.network.DairyFreeDiningApi
import com.kabishan.dairyfreedining.network.Result
import com.kabishan.dairyfreedining.network.httpClient
import io.ktor.http.HttpStatusCode

class SubmissionRepository {

    suspend fun submitFood(
        restaurant: String,
        category: String,
        food: String
    ): Result<Unit> {
        try {
            val response = DairyFreeDiningApi(httpClient).submitFood(
                Submission(restaurant, category, food)
            )

            return if (response.status == HttpStatusCode.OK) {
                Result.success(Unit)
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
