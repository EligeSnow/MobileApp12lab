package com.example.mobileapp12lab.data.remote

import com.example.mobileapp12lab.data.remote.model.BreedDto
import com.example.mobileapp12lab.data.remote.model.DogImageDto
import retrofit2.http.GET
import retrofit2.http.Query

interface DogApi {
    @GET("breeds")
    suspend fun getBreeds(): List<BreedDto>

    @GET("images/search")
    suspend fun searchImages(
        @Query("breed_ids") breedId: Int,
        @Query("limit") limit: Int = 20
    ): List<DogImageDto>
}
