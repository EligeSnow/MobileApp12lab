package com.example.mobileapp12lab.ui.breed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mobileapp12lab.data.BreedRepository

class BreedViewModelFactory(
    private val repository: BreedRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BreedViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return BreedViewModel(repository) as T
        }
        error("Unsupported ViewModel class: ${modelClass.name}")
    }
}
