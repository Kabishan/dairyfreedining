package com.kabishan.dairyfreedining.model

import kotlinx.serialization.Serializable

@Serializable
data class Submission(
    val restaurant: String,
    val category: String,
    val food: String
)