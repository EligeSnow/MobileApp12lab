package com.example.mobileapp12lab.ui.breed

import com.example.mobileapp12lab.data.local.BreedEntity
import com.example.mobileapp12lab.data.remote.model.DogImageDto

data class BreedUiState(
    val breeds: List<BreedEntity> = emptyList(),
    val favoriteBreeds: List<BreedEntity> = emptyList(),
    val selectedBreed: BreedEntity? = null,
    val detailImages: List<DogImageDto> = emptyList(),
    val searchQuery: String = "",
    val isLoading: Boolean = true,
    val isRefreshing: Boolean = false,
    val isDetailLoading: Boolean = false,
    val errorMessage: String? = null,
    val detailErrorMessage: String? = null
)

sealed interface BreedUiEvent {
    data class ShowMessage(val message: String) : BreedUiEvent
}
