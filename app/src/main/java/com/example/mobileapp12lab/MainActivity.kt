package com.example.mobileapp12lab

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mobileapp12lab.data.BreedRepository
import com.example.mobileapp12lab.data.local.BreedEntity
import com.example.mobileapp12lab.ui.breed.BreedListScreen
import com.example.mobileapp12lab.ui.breed.BreedUiState
import com.example.mobileapp12lab.ui.breed.BreedViewModel
import com.example.mobileapp12lab.ui.breed.BreedViewModelFactory
import com.example.mobileapp12lab.ui.navigation.BreedNavigation
import com.example.mobileapp12lab.ui.theme.MobileApp12labTheme
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MobileApp12labTheme(dynamicColor = false, darkTheme = false) {
                var repository by remember { mutableStateOf<BreedRepository?>(null) }
                var initializationError by remember { mutableStateOf<String?>(null) }

                LaunchedEffect(Unit) {
                    try {
                        repository = withContext(Dispatchers.IO) {
                            AppDependencies.breedRepository(applicationContext)
                        }
                    } catch (exception: CancellationException) {
                        throw exception
                    } catch (exception: Exception) {
                        initializationError =
                            exception.message ?: "Unable to initialize local storage."
                    }
                }

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val currentRepository = repository
                    if (currentRepository == null) {
                        StartupContent(errorMessage = initializationError)
                    } else {
                        // Фабрика вручную передаёт ViewModel единую точку доступа к данным.
                        val breedViewModel: BreedViewModel = viewModel(
                            factory = BreedViewModelFactory(currentRepository)
                        )
                        BreedNavigation(viewModel = breedViewModel)
                    }
                }
            }
        }
    }
}

@Composable
private fun StartupContent(errorMessage: String?) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        if (errorMessage == null) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                CircularProgressIndicator()
                Text(
                    text = "Preparing dog breeds...",
                    modifier = Modifier.padding(top = 16.dp)
                )
            }
        } else {
            Text(
                text = "Couldn't prepare the app: $errorMessage",
                modifier = Modifier.padding(24.dp),
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MainActivityPreview() {
    // Preview uses sample data only; it does not run the ViewModel or call the API.
    val breeds = listOf(
        BreedEntity(
            id = 1,
            name = "Golden Retriever",
            temperament = "Friendly, intelligent, devoted",
            origin = "Scotland",
            lifeSpan = "10 - 12 years",
            description = "A friendly and reliable family companion.",
            imageUrl = ""
        ),
        BreedEntity(
            id = 2,
            name = "Labrador Retriever",
            temperament = "Outgoing, even-tempered, gentle",
            origin = "Canada",
            lifeSpan = "10 - 12 years",
            description = "An affectionate and energetic companion.",
            imageUrl = ""
        ),
        BreedEntity(
            id = 3,
            name = "German Shepherd",
            temperament = "Confident, courageous, smart",
            origin = "Germany",
            lifeSpan = "9 - 13 years",
            description = "A versatile working dog and loyal companion.",
            imageUrl = ""
        ),
        BreedEntity(
            id = 4,
            name = "French Bulldog",
            temperament = "Playful, adaptable, smart",
            origin = "France",
            lifeSpan = "10 - 12 years",
            description = "A compact companion with a cheerful personality.",
            imageUrl = ""
        ),
        BreedEntity(
            id = 5,
            name = "Beagle",
            temperament = "Friendly, curious, merry",
            origin = "England",
            lifeSpan = "12 - 15 years",
            description = "A sociable hound with a keen sense of smell.",
            imageUrl = ""
        ),
        BreedEntity(
            id = 6,
            name = "Poodle",
            temperament = "Active, proud, intelligent",
            origin = "Germany",
            lifeSpan = "12 - 15 years",
            description = "An intelligent and active breed.",
            imageUrl = ""
        ),
        BreedEntity(
            id = 7,
            name = "Shiba Inu",
            temperament = "Alert, active, independent",
            origin = "Japan",
            lifeSpan = "13 - 16 years",
            description = "A spirited and good-natured companion.",
            imageUrl = ""
        ),
        BreedEntity(
            id = 8,
            name = "Siberian Husky",
            temperament = "Outgoing, friendly, alert",
            origin = "Siberia",
            lifeSpan = "12 - 14 years",
            description = "An energetic sled dog known for endurance.",
            imageUrl = ""
        ),
        BreedEntity(
            id = 9,
            name = "Dachshund",
            temperament = "Clever, lively, courageous",
            origin = "Germany",
            lifeSpan = "12 - 16 years",
            description = "A small hound with a bold personality.",
            imageUrl = ""
        ),
        BreedEntity(
            id = 10,
            name = "Border Collie",
            temperament = "Energetic, intelligent, responsive",
            origin = "United Kingdom",
            lifeSpan = "12 - 15 years",
            description = "A highly intelligent herding dog.",
            imageUrl = ""
        ),
        BreedEntity(
            id = 11,
            name = "Corgi",
            temperament = "Affectionate, smart, alert",
            origin = "Wales",
            lifeSpan = "12 - 15 years",
            description = "A lively herding breed with a cheerful nature.",
            imageUrl = ""
        ),
        BreedEntity(
            id = 12,
            name = "Boxer",
            temperament = "Bright, fun-loving, active",
            origin = "Germany",
            lifeSpan = "10 - 12 years",
            description = "An energetic and affectionate family dog.",
            imageUrl = ""
        )
    )

    MobileApp12labTheme(dynamicColor = false, darkTheme = false) {
        BreedListScreen(
            uiState = BreedUiState(breeds = breeds),
            searchQuery = "",
            onSearchQueryChange = {},
            onRefresh = {},
            onBreedClick = {},
            onFavoritesClick = {}
        )
    }
}