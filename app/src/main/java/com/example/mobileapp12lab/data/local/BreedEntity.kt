package com.example.mobileapp12lab.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

// Локальная запись породы; isFavorite хранится вместе с остальными данными для офлайн-доступа.
@Entity(tableName = "breeds")
data class BreedEntity(
    @PrimaryKey
    val id: Int,
    val name: String,
    val temperament: String,
    val origin: String?,
    val lifeSpan: String,
    val description: String?,
    val imageUrl: String,
    val isFavorite: Boolean = false
)
