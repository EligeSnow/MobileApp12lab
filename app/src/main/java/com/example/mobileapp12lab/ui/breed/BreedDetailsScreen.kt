package com.example.mobileapp12lab.ui.breed

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
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.mobileapp12lab.data.local.BreedEntity
import com.example.mobileapp12lab.data.remote.model.DogImageDto

@Composable
fun BreedDetailsScreen(
    breed: BreedEntity?,
    images: List<DogImageDto>,
    isLoading: Boolean,
    errorMessage: String?,
    onToggleFavorite: (BreedEntity, Boolean) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (breed == null) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
        return
    }

    val imageUrls = (images.map(DogImageDto::url) + breed.imageUrl)
        .filter(String::isNotBlank)
        .distinct()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text(
            text = breed.name,
            style = MaterialTheme.typography.headlineMedium
        )

        if (imageUrls.isNotEmpty()) {
            val pagerState = rememberPagerState(pageCount = { imageUrls.size })
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(280.dp)
            ) { page ->
                AsyncImage(
                    model = imageUrls[page],
                    contentDescription = "${breed.name} photo ${page + 1}",
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(18.dp)),
                    contentScale = ContentScale.Crop,
                    placeholder = ColorPainter(MaterialTheme.colorScheme.surfaceVariant),
                    error = ColorPainter(MaterialTheme.colorScheme.surfaceVariant)
                )
            }
            Text(
                text = "${pagerState.currentPage + 1} / ${imageUrls.size}",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.labelMedium
            )
        } else if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            Text(
                text = errorMessage ?: "No photos available.",
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Text(
            text = breed.description?.takeIf(String::isNotBlank)
                ?: "No description available.",
            style = MaterialTheme.typography.bodyLarge
        )
        Text(
            text = "Temperament: ${breed.temperament}",
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            text = "Origin: ${breed.origin ?: "Unknown"}",
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            text = "Life span: ${breed.lifeSpan}",
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.weight(1f))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedButton(
                onClick = onBack,
                modifier = Modifier.weight(1f)
            ) {
                Text("Back")
            }
            Button(
                onClick = { onToggleFavorite(breed, !breed.isFavorite) },
                modifier = Modifier.weight(1f)
            ) {
                Text(if (breed.isFavorite) "Remove favorite" else "Add to favorites")
            }
        }
    }
}
