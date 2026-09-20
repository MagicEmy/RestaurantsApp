package com.example.restaurantsapp

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch

class RestaurantDetailsViewModel(private val stateHandle: SavedStateHandle): ViewModel() {
    var state by mutableStateOf(
        RestaurantDetailsScreenState( isLoading = true)
    )

    private val repository = RestaurantDetailsRepository()
    private val errorHandler = CoroutineExceptionHandler { _, exception ->
        exception.printStackTrace()
    }

    init {
        val id = stateHandle.get<Int>("restaurant_id") ?: 0
        viewModelScope.launch(errorHandler) {
            val restaurant = repository.getRestaurantById(id)
            state = state.copy(restaurant = restaurant, isLoading = false)
        }
    }

}
