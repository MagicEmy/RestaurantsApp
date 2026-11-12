package com.example.restaurantsapp.restaurants.presentation.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.restaurantsapp.restaurants.data.di.MainDispatcher
import com.example.restaurantsapp.restaurants.domain.GetInitialRestaurantsUseCase
import com.example.restaurantsapp.restaurants.domain.GetPagedRestaurantsUseCase
import com.example.restaurantsapp.restaurants.domain.Restaurant
import com.example.restaurantsapp.restaurants.domain.ToggleRestaurantUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update



@HiltViewModel
class RestaurantsViewModel @Inject constructor (
    private val getRestaurantsUseCase: GetInitialRestaurantsUseCase,
    private val getPagedRestaurantsUseCase: GetPagedRestaurantsUseCase,
    private val toggleRestaurantUseCase: ToggleRestaurantUseCase,
    @MainDispatcher private val dispatcher : CoroutineDispatcher
): ViewModel() {
    private val _state =
        MutableStateFlow(
            RestaurantsScreenState(
                isLoading = true
            )
        )
    val state: StateFlow<RestaurantsScreenState> =
        _state.asStateFlow()


    val pageRestaurants: Flow<PagingData<Restaurant>> =
        getPagedRestaurantsUseCase()
            .cachedIn(viewModelScope)


    private val errorHandler = CoroutineExceptionHandler { _, exception ->
        exception.printStackTrace()
        _state.update { currentState ->
            currentState.copy(
                error = exception.message,
                isLoading = false
            )
        }
    }

    init {
        getRestaurants()
    }

    private fun getRestaurants() {
        viewModelScope.launch(errorHandler + dispatcher) {
            getRestaurantsUseCase()
            _state.update { currentState ->
                currentState.copy(
                    isLoading = false
                )
            }
        }
    }

    fun retryInitialLoad() {
       _state.update { currentState ->
           currentState.copy(
               isLoading = true,
               error = null
           )
       }
        getRestaurants()
    }

    fun toggleFavorite(id: Int, oldValue: Boolean) {
        viewModelScope.launch(
            errorHandler + dispatcher
        ) {
            toggleRestaurantUseCase(id, oldValue)
        }
    }

}