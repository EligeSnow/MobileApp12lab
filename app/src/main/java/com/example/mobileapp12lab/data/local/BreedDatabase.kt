package com.example.mobileapp12lab.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [BreedEntity::class],
    version = 1,
    exportSchema = false
)
abstract class BreedDatabase : RoomDatabase() {
    abstract fun breedDao(): BreedDao
}
