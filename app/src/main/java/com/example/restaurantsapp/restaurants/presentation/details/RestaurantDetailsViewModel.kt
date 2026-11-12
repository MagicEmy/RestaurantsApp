package com.example.restaurantsapp.restaurants.presentation.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.restaurantsapp.restaurants.domain.Restaurant
import com.example.restaurantsapp.restaurants.data.remote.RestaurantsApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update


@HiltViewModel
class RestaurantDetailsViewModel @Inject constructor(
    private val restInterface: RestaurantsApiService,
    private val stateHandle: SavedStateHandle): ViewModel() {
        private val _state = MutableStateFlow(
            RestaurantDetailsScreenState(isLoading = true))
    val state: StateFlow<RestaurantDetailsScreenState> =
        _state.asStateFlow()

    private val errorHandler = CoroutineExceptionHandler { _, exception ->
        exception.printStackTrace()
    }

    init {
        val id = stateHandle.get<Int>("restaurant_id") ?: 0
        viewModelScope.launch(errorHandler) {
            val restaurant = getRemoteRestaurant(id)
            _state.update { currentState ->
                currentState.copy(
                    restaurant = restaurant,
                    isLoading = false
                )
            }
        }
    }

    private suspend fun getRemoteRestaurant(id: Int): Restaurant? {
        return withContext(Dispatchers.IO) {
            val responseMap = restInterface.getRestaurant(id)
            return@withContext responseMap.values.firstOrNull()?.let { remoteRestaurant ->
                Restaurant(
                    id = remoteRestaurant.id,
                    title = remoteRestaurant.title,
                    description = remoteRestaurant.description
                )
            }
        }
    }
}