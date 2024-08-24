package com.kabishan.dairyfreedining.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Restaurant(
    val id: String,
    @SerialName("image_url")
    val imageUrl: String,
    val name: String
)
