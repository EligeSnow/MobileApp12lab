package com.example.mobileapp12lab.ui.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.example.mobileapp12lab.ui.breed.BreedListScreen
import com.example.mobileapp12lab.ui.breed.BreedDetailsScreen
import com.example.mobileapp12lab.ui.breed.FavoritesScreen
import com.example.mobileapp12lab.ui.breed.BreedUiEvent
import com.example.mobileapp12lab.ui.breed.BreedViewModel
import kotlinx.coroutines.flow.collect

@Composable
fun BreedNavigation(
    viewModel: BreedViewModel,
    modifier: Modifier = Modifier
) {
    val backStack = rememberNavBackStack(BreedListKey)
    val uiState by viewModel.uiState.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val snackbarHostState = SnackbarHostState()

    LaunchedEffect(viewModel) {
        viewModel.events.collect { event ->
            when (event) {
                is BreedUiEvent.ShowMessage ->
                    snackbarHostState.showSnackbar(event.message)
            }
        }
    }

    Scaffold(
        modifier = modifier,
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        NavDisplay(
            backStack = backStack,
            onBack = {
                if (backStack.size > 1) backStack.removeLastOrNull()
            },
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            entryProvider = entryProvider {
                entry<BreedListKey> {
                    BreedListScreen(
                        uiState = uiState,
                        searchQuery = searchQuery,
                        onSearchQueryChange = viewModel::onSearchQueryChange,
                        onRefresh = viewModel::refreshBreeds,
                        onBreedClick = { breed ->
                            backStack.add(BreedDetailsKey(breed.id))
                        },
                        onFavoritesClick = {
                            backStack.add(FavoritesKey)
                        }
                    )
                }
                entry<BreedDetailsKey> { key ->
                    LaunchedEffect(key.breedId) {
                        viewModel.selectBreed(key.breedId)
                    }
                    BreedDetailsScreen(
                        breed = uiState.selectedBreed?.takeIf { it.id == key.breedId },
                        images = uiState.detailImages,
                        isLoading = uiState.isDetailLoading,
                        errorMessage = uiState.detailErrorMessage,
                        onToggleFavorite = viewModel::setFavorite,
                        onBack = { backStack.removeLastOrNull() }
                    )
                }
                entry<FavoritesKey> {
                    FavoritesScreen(
                        breeds = uiState.favoriteBreeds,
                        onBreedClick = { breed ->
                            backStack.add(BreedDetailsKey(breed.id))
                        },
                        onBack = { backStack.removeLastOrNull() }
                    )
                }
            }
        )
    }
}
