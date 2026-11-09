package com.example.mobileapp12lab.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface BreedDao {
    @Query("SELECT * FROM breeds ORDER BY name")
    fun observeBreeds(): Flow<List<BreedEntity>>

    @Query("SELECT * FROM breeds WHERE id = :breedId")
    fun observeBreed(breedId: Int): Flow<BreedEntity?>

    @Query("SELECT * FROM breeds WHERE isFavorite = 1 ORDER BY name")
    fun observeFavorites(): Flow<List<BreedEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBreeds(breeds: List<BreedEntity>)

    @Query("UPDATE breeds SET isFavorite = :isFavorite WHERE id = :breedId")
    suspend fun setFavorite(breedId: Int, isFavorite: Boolean)

    @Query("DELETE FROM breeds WHERE isFavorite = 0")
    suspend fun deleteNonFavorites()
}
