package com.example.mobileapp12lab.ui.breed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobileapp12lab.data.BreedRepository
import com.example.mobileapp12lab.data.local.BreedEntity
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class BreedViewModel(
    private val repository: BreedRepository
) : ViewModel() {
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _uiState = MutableStateFlow(BreedUiState())
    val uiState: StateFlow<BreedUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<BreedUiEvent>()
    val events = _events.asSharedFlow()

    private var refreshJob: Job? = null
    private var detailJob: Job? = null
    private var imageJob: Job? = null

    init {
        observeBreeds()
        observeFavorites()
        refreshBreeds()
    }

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    fun refreshBreeds() {
        if (refreshJob?.isActive == true) return

        refreshJob = viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoading = it.breeds.isEmpty(),
                    isRefreshing = true,
                    errorMessage = null
                )
            }
            try {
                repository.refreshBreeds()
            } catch (exception: CancellationException) {
                throw exception
            } catch (exception: Exception) {
                val message = exception.message ?: "Unable to load breeds."
                _uiState.update { it.copy(errorMessage = message) }
                _events.emit(BreedUiEvent.ShowMessage(message))
            } finally {
                _uiState.update {
                    it.copy(isLoading = false, isRefreshing = false)
                }
            }
        }
    }

    fun selectBreed(breedId: Int) {
        detailJob?.cancel()
        imageJob?.cancel()
        _uiState.update {
            it.copy(
                selectedBreed = null,
                detailImages = emptyList(),
                isDetailLoading = true,
                detailErrorMessage = null
            )
        }
        detailJob = viewModelScope.launch {
            repository.observeBreed(breedId).collect { breed ->
                _uiState.update { it.copy(selectedBreed = breed) }
            }
        }
        imageJob = viewModelScope.launch {
            try {
                val images = repository.getBreedImages(breedId)
                _uiState.update {
                    it.copy(detailImages = images, isDetailLoading = false)
                }
            } catch (exception: CancellationException) {
                throw exception
            } catch (exception: Exception) {
                val message = exception.message ?: "Unable to load breed images."
                _uiState.update {
                    it.copy(isDetailLoading = false, detailErrorMessage = message)
                }
                _events.emit(BreedUiEvent.ShowMessage(message))
            }
        }
    }

    fun setFavorite(breed: BreedEntity, isFavorite: Boolean) {
        viewModelScope.launch {
            try {
                repository.setFavorite(breed.id, isFavorite)
            } catch (exception: CancellationException) {
                throw exception
            } catch (exception: Exception) {
                val message = exception.message ?: "Unable to update favorites."
                _events.emit(BreedUiEvent.ShowMessage(message))
            }
        }
    }

    private fun observeBreeds() {
        viewModelScope.launch {
            combine(repository.observeBreeds(), searchQuery) { breeds, query ->
                breeds.filter { it.name.contains(query.trim(), ignoreCase = true) }
            }.collect { filteredBreeds ->
                _uiState.update { it.copy(breeds = filteredBreeds) }
            }
        }
    }

    private fun observeFavorites() {
        viewModelScope.launch {
            repository.observeFavorites().collect { favorites ->
                _uiState.update { it.copy(favoriteBreeds = favorites) }
            }
        }
    }
}
