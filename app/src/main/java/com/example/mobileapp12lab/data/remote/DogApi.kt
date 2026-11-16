package com.example.mobileapp12lab.data.remote

import com.example.mobileapp12lab.data.remote.model.BreedDto
import com.example.mobileapp12lab.data.remote.model.DogImageDto
import retrofit2.http.GET
import retrofit2.http.Query

// Retrofit строит HTTP-запросы к базовому URL TheDogAPI из AppDependencies.
interface DogApi {
    @GET("breeds")
    suspend fun getBreeds(): List<BreedDto>

    @GET("images/search")
    suspend fun searchImages(
        // API принимает ID породы и ограничение количества фотографий как query-параметры.
        @Query("breed_ids") breedId: Int,
        @Query("limit") limit: Int = 20
    ): List<DogImageDto>
}
