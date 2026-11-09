package com.example.mobileapp12lab.data

import com.example.mobileapp12lab.data.local.BreedEntity
import com.example.mobileapp12lab.data.local.BreedLocalDataSource
import com.example.mobileapp12lab.data.remote.DogRemoteDataSource
import com.example.mobileapp12lab.data.remote.model.BreedDto
import com.example.mobileapp12lab.data.remote.model.DogImageDto
import kotlinx.coroutines.flow.Flow

class BreedRepository(
    private val remoteDataSource: DogRemoteDataSource,
    private val localDataSource: BreedLocalDataSource
) {
    fun observeBreeds(): Flow<List<BreedEntity>> =
        localDataSource.observeBreeds()

    fun observeBreed(breedId: Int): Flow<BreedEntity?> =
        localDataSource.observeBreed(breedId)

    fun observeFavorites(): Flow<List<BreedEntity>> =
        localDataSource.observeFavorites()

    suspend fun refreshBreeds() {
        val breeds = remoteDataSource.getBreeds().map(BreedDto::toEntity)
        localDataSource.replaceBreeds(breeds)
    }

    suspend fun getBreedImages(breedId: Int): List<DogImageDto> =
        remoteDataSource.getBreedImages(breedId)

    suspend fun setFavorite(breedId: Int, isFavorite: Boolean) {
        localDataSource.setFavorite(breedId, isFavorite)
    }
}

private fun BreedDto.toEntity() = BreedEntity(
    id = id,
    name = name,
    temperament = temperament ?: "Unknown",
    origin = origin,
    lifeSpan = lifeSpan ?: "Unknown",
    description = description,
    imageUrl = image?.url
        ?: referenceImageId?.let { "https://cdn2.thedogapi.com/images/$it.jpg" }
        ?: "",
    isFavorite = false
)
