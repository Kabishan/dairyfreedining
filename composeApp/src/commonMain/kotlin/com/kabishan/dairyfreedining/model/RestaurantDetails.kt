package com.kabishan.dairyfreedining.model

import kotlinx.serialization.Serializable

@Serializable
data class RestaurantDetails(
    val categories: Map<String, List<String>>,
    val id: String,
    val name: String
)