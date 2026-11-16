package com.example.mobileapp12lab.ui.breed

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.mobileapp12lab.data.local.BreedEntity
import com.example.mobileapp12lab.ui.theme.MobileApp12labTheme

// Экран получает данные и callbacks извне; бизнес-логика остаётся во ViewModel.
@Composable
fun BreedListScreen(
    uiState: BreedUiState,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onRefresh: () -> Unit,
    onBreedClick: (BreedEntity) -> Unit,
    onFavoritesClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Dog breeds",
                style = MaterialTheme.typography.headlineSmall
            )
            Button(onClick = onFavoritesClick) {
                Text(text = "Favorites")
            }
        }

        OutlinedTextField(
            value = searchQuery,
            onValueChange = onSearchQueryChange,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            singleLine = true,
            label = { Text("Search breeds") }
        )

        // Жест pull-to-refresh повторно запрашивает сеть, а список продолжает показывать Room-кэш.
        PullToRefreshBox(
            isRefreshing = uiState.isRefreshing,
            onRefresh = onRefresh,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            when {
                uiState.isLoading && uiState.breeds.isEmpty() -> LoadingContent()
                uiState.breeds.isEmpty() && uiState.errorMessage != null ->
                    ErrorContent(
                        message = uiState.errorMessage,
                        onRetry = onRefresh
                    )
                uiState.breeds.isEmpty() -> EmptyContent()
                else -> LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(uiState.breeds, key = BreedEntity::id) { breed ->
                        BreedCard(
                            breed = breed,
                            onClick = { onBreedClick(breed) }
                        )
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
private fun LoadingContent() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun ErrorContent(
    message: String,
    onRetry: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Couldn't load breeds",
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            text = message,
            modifier = Modifier.padding(top = 8.dp),
            style = MaterialTheme.typography.bodyMedium
        )
        Button(
            onClick = onRetry,
            modifier = Modifier.padding(top = 12.dp)
        ) {
            Text("Retry")
        }
    }
}

@Composable
private fun EmptyContent() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "No breeds match your search.",
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
private fun BreedCard(
    breed: BreedEntity,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                // Coil загружает изображение по сети/кэшу; цветной painter виден до загрузки или при ошибке.
                model = breed.imageUrl.takeIf(String::isNotBlank),
                contentDescription = "${breed.name} photo",
                modifier = Modifier
                    .size(width = 112.dp, height = 100.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop,
                placeholder = ColorPainter(MaterialTheme.colorScheme.surfaceVariant),
                error = ColorPainter(MaterialTheme.colorScheme.surfaceVariant),
                fallback = ColorPainter(MaterialTheme.colorScheme.surfaceVariant),
                colorFilter = if (breed.imageUrl.isBlank()) {
                    ColorFilter.tint(Color.Gray)
                } else {
                    null
                }
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 14.dp)
            ) {
                Text(
                    text = breed.name,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = breed.temperament.ifBlank { breed.origin ?: "Dog breed" },
                    modifier = Modifier.padding(top = 4.dp),
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun BreedListScreenPreview() {
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
            name = "Shiba Inu",
            temperament = "Alert, active, independent",
            origin = "Japan",
            lifeSpan = "13 - 16 years",
            description = "A spirited and good-natured companion.",
            imageUrl = ""
        )
    )

    MobileApp12labTheme {
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
