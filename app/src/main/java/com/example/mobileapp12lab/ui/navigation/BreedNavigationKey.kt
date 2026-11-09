package com.example.mobileapp12lab.ui.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
data object BreedListKey : NavKey

@Serializable
data class BreedDetailsKey(val breedId: Int) : NavKey

@Serializable
data object FavoritesKey : NavKey
