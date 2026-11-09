package com.example.mobileapp12lab

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mobileapp12lab.ui.breed.BreedViewModel
import com.example.mobileapp12lab.ui.breed.BreedViewModelFactory
import com.example.mobileapp12lab.ui.navigation.BreedNavigation
import com.example.mobileapp12lab.ui.theme.MobileApp12labTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MobileApp12labTheme {
                val breedViewModel: BreedViewModel = viewModel(
                    factory = BreedViewModelFactory(
                        AppDependencies.breedRepository(applicationContext)
                    )
                )
                BreedNavigation(viewModel = breedViewModel)
            }
        }
    }
}