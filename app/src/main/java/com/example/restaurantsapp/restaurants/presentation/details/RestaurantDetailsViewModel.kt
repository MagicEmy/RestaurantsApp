package com.example.restaurantsapp.restaurants.presentation.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.restaurantsapp.restaurants.domain.GetRestaurantByIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update


@HiltViewModel
class RestaurantDetailsViewModel @Inject constructor(
    private val getRestaurantByIdUseCase: GetRestaurantByIdUseCase,
    private val stateHandle: SavedStateHandle): ViewModel() {
        private val _state = MutableStateFlow(
            RestaurantDetailsScreenState(isLoading = true))
    val state: StateFlow<RestaurantDetailsScreenState> =
        _state.asStateFlow()

    init {
        val restaurantId = stateHandle.get<Int>("restaurant_id")

        if (restaurantId == null) {
            _state.update { currentState ->
                currentState.copy(
                    isLoading = false,
                    error = "Restaurant not found."
                )
            }
        } else {
            loadRestaurant(restaurantId)
        }
    }

    private fun loadRestaurant(id: Int) {
        viewModelScope.launch {
            try {
                val restaurant = getRestaurantByIdUseCase(id)

                _state.update { currentState ->
                    currentState.copy(
                        restaurant = restaurant,
                        isLoading = false,
                        error = if (restaurant == null) {
                            "Restaurant not found."
                        } else {
                            null
                        }
                    )
                }
            } catch (exception: CancellationException){
                throw exception
            } catch (exception: Exception) {
                _state.update { currentState ->
                    currentState.copy(
                        isLoading = false,
                        error = "Something went wrong"
                    )
                }
            }
        }
    }
}