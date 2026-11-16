package com.example.mobileapp12lab.ui.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

// Serializable-ключи Navigation 3 описывают экраны и аргументы для восстановления стека.
@Serializable
data object BreedListKey : NavKey

@Serializable
// В ключе деталей передаётся идентификатор выбранной породы.
data class BreedDetailsKey(val breedId: Int) : NavKey

@Serializable
data object FavoritesKey : NavKey
