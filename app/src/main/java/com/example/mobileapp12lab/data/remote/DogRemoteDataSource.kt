package com.example.mobileapp12lab.data.remote

import com.example.mobileapp12lab.data.remote.model.BreedDto
import com.example.mobileapp12lab.data.remote.model.DogImageDto

// Тонкая граница сетевого слоя: детали Retrofit скрыты от репозитория.
class DogRemoteDataSource(
    private val dogApi: DogApi
) {
    suspend fun getBreeds(): List<BreedDto> = dogApi.getBreeds()

    suspend fun getBreedImages(breedId: Int): List<DogImageDto> =
        dogApi.searchImages(breedId = breedId)
}
