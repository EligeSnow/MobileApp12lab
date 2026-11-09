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
                    NavigationPlaceholder(
                        title = "Dog breeds",
                        firstAction = "Open breed details",
                        onFirstAction = {
                            uiState.breeds.firstOrNull()?.let { breed ->
                                backStack.add(BreedDetailsKey(breed.id))
                            }
                        },
                        secondAction = "Favorites",
                        onSecondAction = { backStack.add(FavoritesKey) }
                    )
                }
                entry<BreedDetailsKey> { key ->
                    LaunchedEffect(key.breedId) {
                        viewModel.selectBreed(key.breedId)
                    }
                    NavigationPlaceholder(
                        title = uiState.selectedBreed?.name ?: "Breed details",
                        firstAction = "Back to breeds",
                        onFirstAction = { backStack.removeLastOrNull() }
                    )
                }
                entry<FavoritesKey> {
                    NavigationPlaceholder(
                        title = "Favorites",
                        firstAction = "Back to breeds",
                        onFirstAction = { backStack.removeLastOrNull() }
                    )
                }
            }
        )
    }
}

@Composable
private fun NavigationPlaceholder(
    title: String,
    firstAction: String,
    onFirstAction: () -> Unit,
    secondAction: String? = null,
    onSecondAction: (() -> Unit)? = null
) {
    Column {
        Text(text = title)
        Button(onClick = onFirstAction) {
            Text(text = firstAction)
        }
        if (secondAction != null && onSecondAction != null) {
            Button(onClick = onSecondAction) {
                Text(text = secondAction)
            }
        }
    }
}
