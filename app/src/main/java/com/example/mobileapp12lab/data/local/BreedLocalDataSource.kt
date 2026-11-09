package com.example.mobileapp12lab.data.local

import kotlinx.coroutines.flow.Flow

class BreedLocalDataSource(
    private val breedDao: BreedDao
) {
    fun observeBreeds(): Flow<List<BreedEntity>> = breedDao.observeBreeds()

    fun observeBreed(breedId: Int): Flow<BreedEntity?> =
        breedDao.observeBreed(breedId)

    fun observeFavorites(): Flow<List<BreedEntity>> =
        breedDao.observeFavorites()

    suspend fun replaceBreeds(breeds: List<BreedEntity>) =
        breedDao.replaceBreeds(breeds)

    suspend fun setFavorite(breedId: Int, isFavorite: Boolean) =
        breedDao.setFavorite(breedId, isFavorite)
}
