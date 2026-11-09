package com.example.mobileapp12lab.data.remote.model

import com.squareup.moshi.Json

data class BreedDto(
    val id: Int,
    val name: String,
    val temperament: String? = null,
    val origin: String? = null,
    @param:Json(name = "life_span")
    val lifeSpan: String? = null,
    val description: String? = null,
    @param:Json(name = "reference_image_id")
    val referenceImageId: String? = null,
    val image: DogImageDto? = null
)
