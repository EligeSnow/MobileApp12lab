package com.example.mobileapp12lab

import android.content.Context
import androidx.room.Room
import com.example.mobileapp12lab.data.BreedRepository
import com.example.mobileapp12lab.data.local.BreedDatabase
import com.example.mobileapp12lab.data.local.BreedLocalDataSource
import com.example.mobileapp12lab.data.remote.DogApi
import com.example.mobileapp12lab.data.remote.DogRemoteDataSource
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object AppDependencies {
    private const val BASE_URL = "https://api.thedogapi.com/v1/"
    private const val DATABASE_NAME = "dog_breeds.db"

    @Volatile
    private var repository: BreedRepository? = null

    fun breedRepository(context: Context): BreedRepository =
        repository ?: synchronized(this) {
            repository ?: createRepository(context.applicationContext).also {
                repository = it
            }
        }

    private fun createRepository(context: Context): BreedRepository {
        val database = Room.databaseBuilder(
            context,
            BreedDatabase::class.java,
            DATABASE_NAME
        ).build()
        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okhttp3.OkHttpClient.Builder().build())
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
        val dogApi = retrofit.create(DogApi::class.java)

        return BreedRepository(
            remoteDataSource = DogRemoteDataSource(dogApi),
            localDataSource = BreedLocalDataSource(database.breedDao())
        )
    }
}
