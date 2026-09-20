package com.example.restaurantsapp

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch

class RestaurantDetailsViewModel(private val stateHandle: SavedStateHandle): ViewModel() {
    private val _state = mutableStateOf(
        RestaurantDetailsScreenState( isLoading = true)
    )
    val state: State<RestaurantDetailsScreenState> get() = _state

    private val repository = RestaurantDetailsRepository()
    private val errorHandler = CoroutineExceptionHandler { _, exception ->
        exception.printStackTrace()
    }

    init {
        val id = stateHandle.get<Int>("restaurant_id") ?: 0
        viewModelScope.launch(errorHandler) {
            val restaurant = repository.getRestaurantById(id)
            _state.value = _state.value.copy(restaurant = restaurant, isLoading = false)
        }
    }

}
